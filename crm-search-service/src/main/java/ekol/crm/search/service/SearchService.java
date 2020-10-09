package ekol.crm.search.service;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.facet.FacetResult;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import ekol.crm.search.config.*;
import ekol.crm.search.domain.*;
import ekol.crm.search.domain.account.AccountSearchDoc;
import ekol.crm.search.domain.dto.*;
import ekol.crm.search.domain.dto.Query;
import ekol.crm.search.domain.enumeration.QuoteType;
import ekol.crm.search.domain.opportunity.OpportunitySearchDoc;
import ekol.crm.search.domain.quote.QuoteSearchDoc;
import ekol.crm.search.type.MatchType;
import ekol.crm.search.utils.LanguageStringUtils;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SearchService {

    private ElasticsearchTemplate elasticsearchTemplate;
    private SessionOwner sessionOwner;
    private CompanyService companyService;

    public Page<SearchDoc> searchDocumentByMatchWithAnd(Query query){
    	Integer pageNumber = Optional.ofNullable(query.getPage()).orElse(0).equals(0) ? 1 : query.getPage();
    	Integer pageSize = Optional.ofNullable(query.getSize()).orElse(0).equals(0) ? 10 : query.getSize();

    	List<String> terms = Stream.of(query.getQ().split(" "))
    			.filter(StringUtils::isNotBlank)
    			.map(StringUtils::trim)
    			.map(StringUtils::lowerCase)
    			.map(LanguageStringUtils::setTextForSearch)
    			.collect(Collectors.toList());

    	BoolQueryBuilder qb = QueryBuilders.boolQuery();
    	if(terms.stream().allMatch(NumberUtils::isDigits)) {
    		for (String term : terms) {
    			qb.must(QueryBuilders.boolQuery()
    					.should(QueryBuilders.termQuery("number", Long.valueOf(term)))
    					.should(QueryBuilders.regexpQuery("mappedIds.QUADRO", "[0]*" + Long.valueOf(term))));
    		}
    	} else {
    		for (String term : terms) {
    			qb.should(QueryBuilders.wildcardQuery("textToSearch", StringUtils.appendIfMissing(term, "*")).boost(3f))
    			.should(QueryBuilders.wildcardQuery("accountName", StringUtils.appendIfMissing(term, "*")).boost(2f));
    		}
    		qb.should(QueryBuilders.multiMatchQuery(LanguageStringUtils.setTextForSearch(query.getQ()), "textToSearch","accountName")
    				.analyzer("search_analyzer")
    				.fuzziness(Fuzziness.AUTO)
    				.prefixLength(1)
    				.field("textToSearch", 5f)
    				.field("accountName", 1f));
    	}

    	String[] documentTypes = Optional.ofNullable(query.getDocumentType())
    			.map(Arrays::asList)
    			.orElse(Arrays.asList(DocumentType.values()))
    			.stream()
    			.map(Enum::name)
    			.toArray(String[]::new);

    	SearchQuery searchQuery = new NativeSearchQueryBuilder()
    			.withPageable(new PageRequest(pageNumber - 1, pageSize))
    			.addAggregation(AggregationBuilders.terms("documentType").field("documentType").showTermDocCountError(true))
    			.withQuery(qb)
    			.withTypes(documentTypes)
    			.withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
    			.build();

    	return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, SearchDoc.class, new SearchResultMapper() {
    		@Override
    		public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
    			long totalHits = response.getHits().getTotalHits();
    			List<SearchDoc> values = new ArrayList<>();
    			ObjectMapper mapper = new ObjectMapper();
    			for (SearchHit searchHit : response.getHits()) {
    				SearchDoc doc = mapper.convertValue(searchHit.getSource(), DocumentType.valueOf(searchHit.getType()).getSearchDoc());
    				doc.setScore(searchHit.getScore());
    				values.add(doc);
    			}
    			return new AggregatedPageImpl<>((List<T>) values, pageable, totalHits, response.getAggregations());
    		}
    	}));
    }
    
    private static float decreaseBoost(float boost){
        if(boost >= 3.0f) {
            return 2.0f;
        }else if(boost >= 2.0f){
            return 1.0f;
        }else{
            return 0.5f;
        }
    }
    
    public Page<AccountSearchDoc> moreLikeThis(String q, int page, int size){
        String[] terms = StringUtils.split(q);
        BoolQueryBuilder qb = boolQuery();
        float boost = 3.0f;
        for(String term : terms){
            qb.should(moreLikeThisQuery("name")
                    .like(term).boost(boost)
                    .minTermFreq(1).minDocFreq(1)
                    .maxQueryTerms(12));
            boost = SearchService.decreaseBoost(boost);
        }

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(new PageRequest(page-1, size)).withQuery(qb).build();
        return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, AccountSearchDoc.class));
    }
    
    public Page<AccountSearchDoc> searchAccountsByTerm(Query query){
    	Integer pageNumber = Optional.ofNullable(query.getPage()).orElse(0).equals(0) ? 1 : query.getPage();
        Integer pageSize = Optional.ofNullable(query.getSize()).orElse(0).equals(0) ? 10 : query.getSize();
        
        BoolQueryBuilder qb = boolQuery();
        Stream.of(query.getQ().split(" "))
    			.filter(StringUtils::isNotBlank)
    			.map(StringUtils::trim)
    			.map(StringUtils::lowerCase)
    			.map(LanguageStringUtils::setTextForSearch)
    			.forEach(term->qb.must(QueryBuilders.wildcardQuery("textToSearch","*"+term+"*")));
        
        Optional.ofNullable(query.getWithoutId()).ifPresent(i->matchQuery("id",i));
        Optional.ofNullable(query.getWithoutGlobal()).filter(Boolean::booleanValue).ifPresent(t->qb.mustNot(QueryBuilders.nestedQuery("details",QueryBuilders.termQuery("details.global",t))));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(new PageRequest(pageNumber - 1, pageSize))
                .withQuery(qb)
                .withSort(SortBuilders.fieldSort("textToSearch.raw").order(SortOrder.ASC))
                .build();
        return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, AccountSearchDoc.class));
    }

	public Page<SearchDoc> searchQuoteAsPage(SearchConfig searchConfig, DocumentType[] documents){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(new PageRequest(searchConfig.getPage() - 1, searchConfig.getSize()))
                .withQuery(createQueryBuilder(searchConfig, null))
                .withTypes(Stream.of(documents).map(DocumentType::name).toArray(String[]::new))
                .withSort(SortBuilders.fieldSort("type.code").order(SortOrder.ASC).setNestedPath("type"))
                .withSort(SortBuilders.fieldSort("number").order(SortOrder.DESC))
                .build();
        return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, SearchDoc.class, new SearchResultMapper() {
			
			@Override
			public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
				long totalHits = response.getHits().getTotalHits();
                ObjectMapper mapper = new ObjectMapper();
                List<SearchDoc> values = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                	SearchDoc doc = mapper.convertValue(searchHit.getSource(), DocumentType.valueOf(searchHit.getType()).getSearchDoc());
                	values.add(doc);
                }
                return new AggregatedPageImpl<>((List<T>) values, pageable, totalHits, response.getAggregations());
			}
		}));
    }
    
    public Iterable<SearchDoc> searchQuoteAsList(SearchConfig searchConfig, DocumentType[] documents){
    	List<SearchDoc> result = new ArrayList<>();
    	Page<SearchDoc> searchResult = null;
    	do {
    		searchResult = searchQuoteAsPage(searchConfig, documents);
    		result.addAll(searchResult.getContent());
    		searchConfig.nextPage();
    	}while(searchResult.hasNext());
    	return result;
    }

    //workaround for spring data elastic issue: https://github.com/spring-projects/spring-data-elasticsearch/pull/175
    private <T> Page<T> fixEmptyPage(Page<T> page) {
        AggregatedPageImpl<T> aggregatedPage = (AggregatedPageImpl<T>) page;
        Aggregations aggregations = aggregatedPage.getAggregations();
        if (aggregations == null) {
            Field field = ReflectionUtils.findField(AggregatedPageImpl.class, "aggregations");
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, aggregatedPage, InternalAggregations.EMPTY);

            return aggregatedPage;
        }
        List<FacetResult> facets = aggregatedPage.getFacets();
        if (facets == null || facets.isEmpty()) {
            Field field = ReflectionUtils.findField(AggregatedPageImpl.class, "facets");
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, aggregatedPage, InternalAggregations.EMPTY);
            return aggregatedPage;
        }
        return page;
    }

    public QuoteHomePageStatistics getQuoteHomePageStatistics(QuoteType quoteType) {

        Map<String, Long> quoteMap = getQuotesBasedOnServiceArea(quoteType);

        long totalCount = quoteMap.values().stream().mapToLong(Long::valueOf).sum();

        List<ServiceAreaBasedCount> serviceAreaBasedCounts = new ArrayList<>();

        quoteMap.entrySet().forEach(entry -> serviceAreaBasedCounts.add(new ServiceAreaBasedCount(entry.getKey().toUpperCase(), entry.getValue())));

        return new QuoteHomePageStatistics(totalCount, serviceAreaBasedCounts);
    }

    private Map<String, Long> getQuotesBasedOnServiceArea(QuoteType quoteType) {

        String username = sessionOwner.getCurrentUser().getUsername();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                                                         .minimumNumberShouldMatch(1)
                                                         .should(QueryBuilders.matchQuery("accountOwner", username))
                                                         .should(QueryBuilders.matchQuery("createdBy", username))
                                                         .must(QueryBuilders.nestedQuery("status",
                                                                 QueryBuilders.boolQuery()
                                                                         .minimumNumberShouldMatch(1)
                                                                         .should(QueryBuilders.matchQuery("status.code", "OPEN"))
                                                                         .should(QueryBuilders.matchQuery("status.code", "PDF_CREATED"))))
                                                         .must(QueryBuilders.nestedQuery("type",
                                                                 QueryBuilders.boolQuery()
                                                                         .must(QueryBuilders.matchQuery("type.code", quoteType))));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("crm")
                .withTypes(DocumentType.quote.name())
                .withQuery(boolQueryBuilder)
                .addAggregation(AggregationBuilders
                        .nested("serviceAreaAgg")
                        .path("serviceArea")
                        .subAggregation(AggregationBuilders.terms("serviceAreaCodeAgg").field("serviceArea.code")
                                .subAggregation(AggregationBuilders.count("count_agg").field("serviceArea.code")))
                        ).build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);

        Nested aggregation = aggregations.get("serviceAreaAgg");
        return ((Terms) aggregation.getAggregations().get("serviceAreaCodeAgg")).getBuckets()
                .stream()
                .collect(Collectors.toMap(Terms.Bucket::getKeyAsString, Terms.Bucket::getDocCount));
    }

	public OpportunityHomePageStatistics getOpportunityHomePageStatistic(){
		Map<String, Long> opportunityMap = getOpportunityBasedOnServiceArea();

		long totalCount = opportunityMap.values().stream().mapToLong(Long::valueOf).sum();

		List<ServiceAreaBasedCount> serviceAreaBasedCounts = new ArrayList<>();

		opportunityMap.entrySet().forEach(entry -> serviceAreaBasedCounts.add(new ServiceAreaBasedCount(entry.getKey().toUpperCase(), entry.getValue())));

		return new OpportunityHomePageStatistics(totalCount, serviceAreaBasedCounts);
	}

	private Map<String, Long> getOpportunityBasedOnServiceArea(){
		String username = sessionOwner.getCurrentUser().getUsername();

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
				.minimumNumberShouldMatch(1)
				.should(QueryBuilders.matchQuery("accountOwner", username))
				.should(QueryBuilders.matchQuery("createdBy", username))
				.must(QueryBuilders.nestedQuery("status",
						QueryBuilders.boolQuery()
								.minimumNumberShouldMatch(1)
								.should(QueryBuilders.matchQuery("status.code", "PROSPECTING"))
								.should(QueryBuilders.matchQuery("status.code", "VALUE_PROPOSITION"))
								.should(QueryBuilders.matchQuery("status.code", "QUOTED"))));

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withIndices("crm")
				.withTypes(DocumentType.opportunity.name())
				.withQuery(boolQueryBuilder)
				.addAggregation(AggregationBuilders
						.nested("serviceAreaAgg")
						.path("serviceArea")
						.subAggregation(AggregationBuilders.terms("serviceAreaCodeAgg").field("serviceArea.code")
								.subAggregation(AggregationBuilders.count("count_agg").field("serviceArea.code")))
				).build();

		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);

		Nested aggregation = aggregations.get("serviceAreaAgg");
		return ((Terms) aggregation.getAggregations().get("serviceAreaCodeAgg")).getBuckets()
				.stream()
				.collect(Collectors.toMap(Terms.Bucket::getKeyAsString, Terms.Bucket::getDocCount));
	}

	public String searchAccountOwner(Long accId) {
        StringQuery stringQuery = new StringQuery(termQuery("id", accId).toString());

        AccountSearchDoc accountSearchDoc = elasticsearchTemplate.queryForObject(stringQuery, AccountSearchDoc.class);

        return Optional.ofNullable(accountSearchDoc).map(AccountSearchDoc::getAccountOwner).orElse(null);
    }

    public Page<QuoteSearchDoc> searchQuotes(QuoteSearchJson search, boolean forHomepage) {
    	Integer pageSize = 10;

    	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

    	if(forHomepage) {
    		boolQueryBuilder.minimumNumberShouldMatch(1);
    		if(!CollectionUtils.isEmpty(search.getTeammates())) {
    			search.getTeammates().forEach(user->boolQueryBuilder
    					.should(QueryBuilders.matchQuery("accountOwner", user))
    					.should(QueryBuilders.matchQuery("createdBy", user))
    			);
    		} else {
    			String username = sessionOwner.getCurrentUser().getUsername();
    			boolQueryBuilder
    					.should(QueryBuilders.matchQuery("accountOwner", username))
    					.should(QueryBuilders.matchQuery("createdBy", username));
    		}
    	}

    	Optional.ofNullable(search.getAccountId()).ifPresent(id -> boolQueryBuilder
    			.must(QueryBuilders.nestedQuery("account",
    					QueryBuilders.boolQuery()
    					.must(QueryBuilders.matchQuery("account.id", search.getAccountId())))));

    	Optional.ofNullable(search.getNumber()).ifPresent(n -> boolQueryBuilder
    			.must(termQuery("number", search.getNumber())));

    	Optional.ofNullable(search.getCreatedBy()).ifPresent(createdBy->boolQueryBuilder
    			.must(termQuery("createdBy", createdBy)));

    	Optional.ofNullable(search.getTypeCode()).ifPresent(t -> boolQueryBuilder
    			.must(QueryBuilders.nestedQuery("type",
    					QueryBuilders.boolQuery()
    					.must(QueryBuilders.matchQuery("type.code", search.getTypeCode())))));

    	if(Optional.ofNullable(search.getStatusCode()).isPresent()){
            boolQueryBuilder
                    .must(QueryBuilders.nestedQuery("status",
                            QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery("status.code", search.getStatusCode()))));
        }else{
            boolQueryBuilder
                    .must(QueryBuilders.nestedQuery("status",
                            QueryBuilders.boolQuery()
                                    .mustNot(QueryBuilders.matchQuery("status.code", "CANCELED"))));
        }

    	if (Optional.ofNullable(search.getMinUpdateDate()).isPresent() || Optional.ofNullable(search.getMaxUpdateDate()).isPresent()) {
    		String maxUpdateDate = Optional.ofNullable(search.getMaxUpdateDate()).map(m -> m.concat(" 23:59")).orElse(null);
    		String minUpdateDate = Optional.ofNullable(search.getMinUpdateDate()).map(m -> m.concat(" 00:00")).orElse(null);

    		boolQueryBuilder.must(QueryBuilders.rangeQuery("lastUpdated")
    				.lte(maxUpdateDate)
    				.gte(minUpdateDate));
    	}

    	if (Optional.ofNullable(search.getMinCreatedAt()).isPresent() || Optional.ofNullable(search.getMaxCreatedAt()).isPresent()) {
    		String maxCreatedAt= Optional.ofNullable(search.getMaxCreatedAt()).map(m -> m.concat(" 23:59")).orElse(null);
    		String minCreatedAt = Optional.ofNullable(search.getMinCreatedAt()).map(m -> m.concat(" 00:00")).orElse(null);

    		boolQueryBuilder.must(QueryBuilders.rangeQuery("createdAt")
    				.lte(maxCreatedAt)
    				.gte(minCreatedAt));
    	}

    	Optional.ofNullable(search.getServiceAreaCode()).ifPresent(code -> boolQueryBuilder
    			.must(QueryBuilders.nestedQuery("serviceArea",
    					QueryBuilders.boolQuery()
    					.must(QueryBuilders.matchQuery("serviceArea.code", search.getServiceAreaCode())))));

    	Optional.ofNullable(search.getShipmentLoadingType()).ifPresent(code -> boolQueryBuilder
    			.must(QueryBuilders.nestedQuery("products.shipmentLoadingType",
    					QueryBuilders.boolQuery()
    					.must(QueryBuilders.matchQuery("products.shipmentLoadingType.code", code)))));

    	Optional.ofNullable(search.getFromCountry()).ifPresent(code -> boolQueryBuilder
    			.must(QueryBuilders.nestedQuery("products.fromCountry",
    					QueryBuilders.boolQuery()
    					.must(QueryBuilders.matchQuery("products.fromCountry.iso", code)))));

    	Optional.ofNullable(search.getToCountry()).ifPresent(code -> boolQueryBuilder
    			.must(QueryBuilders.nestedQuery("products.toCountry",
    					QueryBuilders.boolQuery()
    					.must(QueryBuilders.matchQuery("products.toCountry.iso", code)))));

    	Optional.ofNullable(search.getFromPoint()).ifPresent(code -> boolQueryBuilder
    			.must(QueryBuilders.nestedQuery("products.fromPoint",
    					QueryBuilders.boolQuery()
    					.must(QueryBuilders.matchQuery("products.fromPoint.id", code)))));

    	Optional.ofNullable(search.getToPoint()).ifPresent(code -> boolQueryBuilder
    			.must(QueryBuilders.nestedQuery("products.toPoint",
    					QueryBuilders.boolQuery()
    					.must(QueryBuilders.matchQuery("products.toPoint.id", code)))));

    	Optional.ofNullable(search.getQuoteAttributeKey()).ifPresent(key ->
				Optional.ofNullable(search.getQuoteAttributeValue()).ifPresent(value -> boolQueryBuilder
				.must(QueryBuilders.matchQuery(("quoteAttribute." + key), value))));

    	SearchQuery searchQuery = new NativeSearchQueryBuilder()
    			.withPageable(new PageRequest(search.getPage(), pageSize))
    			.withQuery(boolQueryBuilder)
    			.withTypes(DocumentType.quote.name())
    			.withSort(SortBuilders.fieldSort("lastUpdated").order(SortOrder.DESC))
    			.build();


    	return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, QuoteSearchDoc.class));
    }

    public Page<AccountSearchDoc> searchAccounts(AccountSearchJson search) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		Optional.ofNullable(search.getAccountOwners()).ifPresent(owners -> {
			owners.forEach(o -> boolQueryBuilder.should(QueryBuilders.matchQuery("accountOwner", o)));
		});

		Optional.ofNullable(search.getName()).ifPresent(name -> {
			Stream.of(name.split(" "))
					.filter(StringUtils::isNotBlank)
					.map(StringUtils::trim)
					.map(StringUtils::lowerCase)
					.map(LanguageStringUtils::setTextForSearch)
					.forEach(term->boolQueryBuilder.must(QueryBuilders.wildcardQuery("textToSearch","*"+term+"*")));
		});

		Optional.ofNullable(search.getAccountTypeCode()).ifPresent(accType -> boolQueryBuilder.must(QueryBuilders.matchQuery("accountType.code", accType)));
		Optional.ofNullable(search.getCompanyId()).ifPresent(companyId -> boolQueryBuilder.must(QueryBuilders.matchQuery("company.id", companyId)));
		Optional.ofNullable(search.getCountryIso()).ifPresent(countryIso -> boolQueryBuilder.must(QueryBuilders.matchQuery("country.iso", countryIso)));
		Optional.ofNullable(search.getCreatedBy()).ifPresent(createdBy -> boolQueryBuilder.must(QueryBuilders.matchQuery("createdBy", createdBy)));
		Optional.ofNullable(search.getParentSectorCode()).ifPresent(psCode -> boolQueryBuilder.must(QueryBuilders.matchQuery("parentSector.code", psCode)));
		Optional.ofNullable(search.getSubSectorCode()).ifPresent(ssCode -> boolQueryBuilder.must(QueryBuilders.matchQuery("subSector.code", ssCode)));

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withPageable(new PageRequest(search.getPage(), search.getPageSize()))
				.withQuery(boolQueryBuilder)
				.withTypes(DocumentType.account.name())
				.withSort(SortBuilders.fieldSort("name").order(SortOrder.ASC))
				.build();

		return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, AccountSearchDoc.class));
	}

    public Page<AccountSearchDoc> searchAccountsForHomePage(SearchConfig searchConfig) {

        String username = sessionOwner.getCurrentUser().getUsername();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(termQuery("accountOwner", username));

        createQueryBuilder(searchConfig, boolQueryBuilder);

		if (searchConfig.getMatchFilters().stream().anyMatch(s -> (s.getName().equalsIgnoreCase("minUpdateDate") || s.getName().equalsIgnoreCase("maxUpdateDate")))) {
			searchMinAndMaxDateOfField("updateDate", "lastUpdated", searchConfig, boolQueryBuilder);
		}

		if (searchConfig.getMatchFilters().stream().anyMatch(s -> (s.getName().equalsIgnoreCase("minCreatedAt") || s.getName().equalsIgnoreCase("maxCreatedAt")))) {
			searchMinAndMaxDateOfField("createdAt", "createdAt", searchConfig, boolQueryBuilder);
		}
        
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(new PageRequest(searchConfig.getPage(), searchConfig.getSize()))
                .withQuery(boolQueryBuilder)
                .withTypes(DocumentType.account.name())
                .withSort(SortBuilders.fieldSort("lastUpdated").order(SortOrder.DESC))
                .build();

        return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, AccountSearchDoc.class));
    }
    
    public AccountHomePageStatistics getAccountHomePageStatistics() {
    	Map<String, Long> quoteMap = getAccountBasedOnAccountType();

    	long totalCount = quoteMap.values().parallelStream().mapToLong(Long::valueOf).sum();
    	List<AccountTypeBasedCount> accountTypeBasedCounts = new ArrayList<>();

    	quoteMap.entrySet().forEach(entry -> accountTypeBasedCounts.add(new AccountTypeBasedCount(entry.getKey().toUpperCase(), entry.getValue())));

    	return new AccountHomePageStatistics(totalCount, accountTypeBasedCounts);
    }

	public Page<OpportunitySearchDoc> searchOpportunitiesForHomePage(SearchConfig searchConfig){
		String username = sessionOwner.getCurrentUser().getUsername();

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		if (!CollectionUtils.isEmpty(searchConfig.getTeammates())) {
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			for (String user : searchConfig.getTeammates()) {
				boolQueryBuilder2
						.should(QueryBuilders.matchQuery("accountOwner", user))
						.should(QueryBuilders.matchQuery("createdBy", user));
			}
			boolQueryBuilder.must(boolQueryBuilder2);
		} else {
			boolQueryBuilder.must(QueryBuilders.boolQuery()
					.should(QueryBuilders.matchQuery("accountOwner", username))
					.should(QueryBuilders.matchQuery("createdBy", username)));
		}

		createQueryBuilder(searchConfig, boolQueryBuilder);

		if (searchConfig.getMatchFilters().stream().anyMatch(s -> (s.getName().equalsIgnoreCase("minUpdateDate") || s.getName().equalsIgnoreCase("maxUpdateDate")))) {
			searchMinAndMaxDateOfField("updateDate", "lastUpdated", searchConfig, boolQueryBuilder);
		}

		if (searchConfig.getMatchFilters().stream().anyMatch(s -> (s.getName().equalsIgnoreCase("minCreateDate") || s.getName().equalsIgnoreCase("maxCreateDate")))) {
			searchMinAndMaxDateOfField("createDate", "createdAt", searchConfig, boolQueryBuilder);
		}

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withPageable(new PageRequest(searchConfig.getPage(), searchConfig.getSize()))
				.withQuery(boolQueryBuilder)
				.withTypes(DocumentType.opportunity.name())
				.withSort(SortBuilders.fieldSort("lastUpdated").order(SortOrder.DESC))
				.build();

		return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, OpportunitySearchDoc.class));
	}

	private BoolQueryBuilder searchMinAndMaxDateOfField(String query, String field, SearchConfig searchConfig, BoolQueryBuilder boolQueryBuilder){
		String min = "min" + StringUtils.capitalize(query);
		String max = "max" + StringUtils.capitalize(query);

		MatchFilter minUpdateDateFilter = searchConfig.getMatchFilters().stream().filter(s -> s.getName().equalsIgnoreCase(min)).findAny().orElse(null);
		MatchFilter maxUpdateDateFilter = searchConfig.getMatchFilters().stream().filter(s -> s.getName().equalsIgnoreCase(max)).findAny().orElse(null);

		String minUpdateDateStr = Optional.ofNullable(minUpdateDateFilter).map(m -> m.getVal().concat(" 00:00")).orElse(null);
		String maxUpdateDateStr = Optional.ofNullable(maxUpdateDateFilter).map(m -> m.getVal().concat(" 23:59")).orElse(null);

		boolQueryBuilder.must(QueryBuilders.rangeQuery(field)
				.lte(maxUpdateDateStr)
				.gte(minUpdateDateStr));
		return boolQueryBuilder;
	}

	private BoolQueryBuilder createQueryBuilder(SearchConfig searchConfig, BoolQueryBuilder queryBuilder) {
    	BoolQueryBuilder boolQueryBuilder = Optional.ofNullable(queryBuilder).orElseGet(QueryBuilders::boolQuery);
    	searchConfig.getMatchFilters().forEach(mt-> {
			Optional.ofNullable(MatchType.fromName(mt.getName())).ifPresent(matchType -> {
				QueryBuilder query = matchType.buildQuery(mt.getVal(), mt.getOperator());
				if (mt.isNot()) {
					boolQueryBuilder.mustNot(query);
				} else {
					boolQueryBuilder.must(query);
				}
			});
    	});
    	return boolQueryBuilder;
    }

    private List<Long> findCompanyIdsByCityAndDistrict(String city, String district) {
        List<Long> ids = new ArrayList<>();
        CompanySearchIterator iterator = new CompanySearchIterator(companyService, city, district, true, 1000);
        while (iterator.hasNext()) {
            ids.add(iterator.next());
        }
        return ids;
    }
    
    
	
    private Map<String, Long> getAccountBasedOnAccountType() {

    	String username = sessionOwner.getCurrentUser().getUsername();

    	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
    			.minimumNumberShouldMatch(1)
    			.should(QueryBuilders.matchQuery("accountOwner", username));

    	SearchQuery searchQuery = new NativeSearchQueryBuilder()
    			.withIndices("crm")
    			.withTypes(DocumentType.account.name())
    			.withQuery(boolQueryBuilder)
    			.addAggregation(AggregationBuilders
    					.nested("accountTypeAgg")
    					.path("accountType")
    					.subAggregation(AggregationBuilders.terms("accountTypeCodeAgg").field("accountType.name")
    							.subAggregation(AggregationBuilders.count("count_agg").field("accountType.name")))
    					).build();

    	Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);

    	Nested aggregation = aggregations.get("accountTypeAgg");
    	return ((Terms) aggregation.getAggregations().get("accountTypeCodeAgg")).getBuckets()
    			.stream()
    			.collect(Collectors.toMap(Terms.Bucket::getKeyAsString, Terms.Bucket::getDocCount));
    }
	
}