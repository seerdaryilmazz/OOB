package ekol.kartoteks.service;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.MatchQueryBuilder.Type;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.fieldvaluefactor.FieldValueFactorFunctionBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.facet.FacetResult;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.search.CompanySearchDoc;
import ekol.kartoteks.utils.LanguageStringUtils;

/**
 * Created by kilimci on 29/04/16.
 */
@Service
public class SearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Page<CompanySearchDoc> searchCompanyPrefix(String q, Boolean shortNameChecked, Integer page, Integer size) {

        if (StringUtils.isBlank(q) && shortNameChecked == null) {
            throw new BadRequestException("There must be at least one search criteria.");
        }

        Integer pageNumber = page == null || page.equals(0) ? 1 : page;
        Integer pageSize = size == null || size.equals(0) ? 20 : size;

        List<String> terms = Stream.of(q.split(" "))
    			.filter(StringUtils::isNotBlank)
    			.map(StringUtils::trim)
    			.map(StringUtils::lowerCase)
    			.map(LanguageStringUtils::setTextForSearch)
    			.collect(Collectors.toList());
    	
        float boost = terms.size();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.should(QueryBuilders.matchQuery("nameToSearch", LanguageStringUtils.setTextForSearch(q))
				.analyzer("autocomplete_analyzer")
				.fuzziness(Fuzziness.AUTO)
				.prefixLength(1)
				.boost(boost+1f));
		
		for (String term : terms) {
			query.should(QueryBuilders.wildcardQuery("nameToSearch", StringUtils.appendIfMissing(term, "*")).boost(boost--));
		}
        
        Optional.ofNullable(shortNameChecked)
        	.map(x->query.should(QueryBuilders.matchQuery("shortNameChecked", x)))
        	.ifPresent(query::should);
        
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(new PageRequest(pageNumber - 1, pageSize))
                .withQuery(query)
                .withMinScore(1f)
                ;

        return fixEmptyPage(elasticsearchTemplate.queryForPage(queryBuilder.build(), CompanySearchDoc.class, new SearchResultMapper() {
			
			@Override
			public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
				ObjectMapper mapper = new ObjectMapper();
				List<CompanySearchDoc> values = new ArrayList<>();
				for (SearchHit hit : response.getHits()) {
					CompanySearchDoc doc = mapper.convertValue(hit.getSource(),CompanySearchDoc.class);
					doc.setScore(hit.getScore());
					values.add(doc);
				}
				return new AggregatedPageImpl<>((List<T>) values, pageable, response.getHits().getTotalHits(), response.getAggregations());
			}
		}));
    }

    public Page<CompanySearchDoc> searchCompanyMatchWithAnd(String q, Integer page, Integer size){
        Integer pageNumber = page == null || page.equals(0) ? 1 : page;
        Integer pageSize = size == null || size.equals(0) ? 10 : size;
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(new PageRequest(pageNumber - 1, pageSize))
                .withSort(SortBuilders.fieldSort("name.raw"))
                .withQuery(matchQuery("name", q.toLowerCase()).operator(MatchQueryBuilder.Operator.AND))
                .build();
        return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, CompanySearchDoc.class));
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


    public Page<CompanySearchDoc> searchByCountry(String country, Integer page, Integer size){
        Integer pageNumber = page == null || page.equals(0) ? 1 : page;
        Integer pageSize = size == null || size.equals(0) ? 1 : size;

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(new PageRequest(pageNumber - 1, pageSize))
                .withSort(SortBuilders.fieldSort("name.raw"))
                .withQuery(termQuery("countryName", country))
                .build();
        return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, CompanySearchDoc.class));
    }

    public Page<CompanySearchDoc> moreLikeThis(String q, String taxId, String taxOfficeCode, int page, int size){
        String[] terms = StringUtils.split(q);
        BoolQueryBuilder qb = boolQuery();
        float boost = 3.0f;
        float taxIdBoost = 10.0f;
        for(String term : terms){
            qb.should(moreLikeThisQuery("name")
                    .like(term).boost(boost)
                    .minTermFreq(1).minDocFreq(1)
                    .maxQueryTerms(12));
            boost = SearchService.decreaseBoost(boost);
        }
        if(StringUtils.isNotBlank(taxId)){
            qb.should(matchQuery("taxId", taxId).boost(taxIdBoost));
        }
        if(StringUtils.isNotBlank(taxOfficeCode)){
            qb.should(matchQuery("taxOfficeCode", taxOfficeCode));
        }
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(new PageRequest(page-1, size)).withQuery(qb).build();
        return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, CompanySearchDoc.class));
    }

    public Map<String, Long> aggregateCountry(){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .addAggregation(AggregationBuilders.terms("countries").field("countryName"))
                .build();
        Map<String, Long> aggregations = new HashMap<>();
        StringTerms result = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations().get("countries"));
        for (Terms.Bucket entry : result.getBuckets()) {
            aggregations.put(entry.getKeyAsString(), entry.getDocCount());
        }

        return aggregations;

    }

    /**
     * TODO:
     * Bu metod önceden EditCompanyShortNames.jsx sayfasında kullanılıyordu,
     * sayfadaki aramayı searchCompanyPrefix metodunu kullanacak şekilde değiştirdik,
     * searchCompanyPrefix metodunu da shortNameChecked alanı için güncelledik.
     * Ancak searchCompanyPrefix metodunda statsCustomer ile ilgili bir güncelleme yapmadık çünkü
     * statsCustomer alanının nerede ve nasıl güncellendiğini bulamadık,
     * statsCustomer (ve statsParticipant) yarım kalmış bir çalışma olabilir mi?
     */
    public Page<CompanySearchDoc> searchUncheckedShortName(String q, boolean shortNameChecked, Integer page, Integer size){
        Integer pageNumber = page == null || page.equals(0) ? 1 : page;
        Integer pageSize = size == null || size.equals(0) ? 10 : size;

        MatchQueryBuilder shortNameCheckedQuery = matchQuery("shortNameChecked", shortNameChecked);
        BoolQueryBuilder andQueryBuilder = boolQuery().must(shortNameCheckedQuery);
        if(StringUtils.isNotBlank(q)){
            andQueryBuilder.must(matchQuery("name", q.toLowerCase()).operator(MatchQueryBuilder.Operator.AND));
        }
        FunctionScoreQueryBuilder scoreQuery =
                functionScoreQuery(andQueryBuilder,
                        new FieldValueFactorFunctionBuilder("statsCustomer").factor(2.0f));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(new PageRequest(pageNumber - 1, pageSize))
                .withQuery(scoreQuery)
                .build();
        return fixEmptyPage(elasticsearchTemplate.queryForPage(searchQuery, CompanySearchDoc.class));
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

    public Page<Long> searchCompanyIdsByCityAndDistrict(
            String city, String district, boolean searchOnlyInDefaultLocations, Integer page, Integer size) {

        if (StringUtils.isBlank(city)) {
            throw new BadRequestException("City criteria must be specified.");
        }

        Pageable pageable = new PageRequest(page == null ? 0 : page, size == null ? 20 : size);
        BoolQueryBuilder qb = boolQuery();

        String[] wordsOfCity = LanguageStringUtils.stripAccents(city).toLowerCase().split("\\W");
        for (String word : wordsOfCity) {
            if (StringUtils.isNotBlank(word)) {
                NestedQueryBuilder cityQueryBuilder = nestedQuery("locations", prefixQuery("locations.cityAccentsStripped", word.trim()));
                qb.must(cityQueryBuilder);
            }
        }

        if (StringUtils.isNotBlank(district)) {
            String[] wordsOfDistrict = LanguageStringUtils.stripAccents(district).toLowerCase().split("\\W");
            for (String word : wordsOfDistrict) {
                if (StringUtils.isNotBlank(word)) {
                    NestedQueryBuilder districtQueryBuilder = nestedQuery("locations", prefixQuery("locations.districtAccentsStripped", word.trim()));
                    qb.must(districtQueryBuilder);
                }
            }
        }

        if (searchOnlyInDefaultLocations) {
            NestedQueryBuilder isDefaultQueryBuilder = nestedQuery("locations", termQuery("locations.default", true));
            qb.must(isDefaultQueryBuilder);
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(qb)
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("id").build())
                .withSort(SortBuilders.fieldSort("name.raw"));

        Page<CompanySearchDoc> pagedDocuments = fixEmptyPage(elasticsearchTemplate.queryForPage(queryBuilder.build(), CompanySearchDoc.class));

        List<Long> ids = new ArrayList<>();
        for (CompanySearchDoc doc : pagedDocuments.getContent()) {
            ids.add(doc.getId());
        }

        return new PageImpl<>(ids, pageable, pagedDocuments.getTotalElements());
    }
}
