package ekol.crm.search.service;

import java.util.*;

import javax.transaction.Transactional;

import org.apache.commons.collections4.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.*;

import ekol.crm.search.client.*;
import ekol.crm.search.config.*;
import ekol.crm.search.domain.*;
import ekol.crm.search.domain.account.*;
import ekol.crm.search.domain.agreement.AgreementSearchDoc;
import ekol.crm.search.domain.dto.CompanyLocation;
import ekol.crm.search.domain.opportunity.OpportunitySearchDoc;
import ekol.crm.search.domain.quote.QuoteSearchDoc;
import ekol.crm.search.event.dto.account.*;
import ekol.crm.search.event.dto.agreement.AgreementJson;
import ekol.crm.search.event.dto.opportunity.OpportunityJson;
import ekol.crm.search.event.dto.quote.QuoteJson;
import ekol.crm.search.type.MatchType;
import ekol.resource.oauth2.SessionOwner;

@Service
public class IndexingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexingService.class);
	
	@Autowired
	private SessionOwner sessionOwner;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private SearchService searchService;
	
	@Autowired 
	private CrmAccountServiceClient crmAccountServiceClient;
	
	@Autowired 
	private CrmQuoteServiceClient crmQuoteServiceClient;

	@Autowired
	private AgreementServiceClient agreementServiceClient;

	@Autowired
	private CrmOpportunityServiceClient crmOpportunityServiceClient;
	
	@Autowired
	private KartoteksServiceClient kartoteksServiceClient;

	@Value("${oneorder.crm-account-service}")
	private String crmAccountServiceName;

	@Value("${oneorder.crm-quote-service}")
	private String crmQuoteServiceName;

	@Value("${oneorder.agreement-service}")
	private String agreementServiceName;

	@Value("${oneorder.crm-opportunity-service}")
	private String opportunityServiceName;

	@Autowired
	private RestTemplate restTemplate;

	@JsonIgnoreProperties(ignoreUnknown = true)
	static class RestResponsePage<T> extends PageImpl<T> {

		@JsonCreator
		public RestResponsePage(@JsonProperty("content") List<T> content,
				@JsonProperty("number") int number,
				@JsonProperty("size") int size,
				@JsonProperty("totalElements") Long totalElements) {
			super(content, new PageRequest(number, size), totalElements);
		}
	}
	
	public String deleteDocument(Long id, DocumentType document) {
		elasticsearchTemplate.delete(document.getSearchDoc(), String.valueOf(id));
		return String.format("%s data has been deleted successfully.", document.name());
	}
	
	@Transactional
	public void createIndex() {
		if(!elasticsearchTemplate.indexExists(SearchDoc.class)){
			elasticsearchTemplate.createIndex(SearchDoc.class);
		}
	}

	@Transactional
	public void deleteIndex() {
		elasticsearchTemplate.deleteIndex(SearchDoc.class);
		elasticsearchTemplate.createIndex(SearchDoc.class);
		elasticsearchTemplate.putMapping(AccountSearchDoc.class);
		elasticsearchTemplate.putMapping(ContactSearchDoc.class);
		elasticsearchTemplate.putMapping(QuoteSearchDoc.class);
		elasticsearchTemplate.putMapping(AgreementSearchDoc.class);
		elasticsearchTemplate.putMapping(OpportunitySearchDoc.class);
		elasticsearchTemplate.refresh(SearchDoc.class);
	}
	
	public void putType(DocumentType documentType) {
		Document document = documentType.getSearchDoc().getAnnotation(Document.class);
		if(!elasticsearchTemplate.typeExists(document.indexName(), document.type())){
			elasticsearchTemplate.putMapping(documentType.getSearchDoc());
			elasticsearchTemplate.refresh(documentType.getSearchDoc());
		}
	}

	@Transactional
	public void indexImportedData(DocumentType data, Integer pageBlock, Integer pageSize){
		String uuid = UUID.randomUUID().toString();
		try {

			LOGGER.info("Indexing is started by {}. uuid: {}", sessionOwner.getCurrentUser().getUsername(), uuid);
			if(Objects.isNull(data)) {
				LOGGER.info("Index is deleted. uuid: {}", uuid);
				deleteIndex();
			}

			if(Objects.isNull(data) || DocumentType.account == data) {
				indexImportedAccount(pageBlock, pageSize,  uuid);
			}

			if(Objects.isNull(data) || DocumentType.quote == data) {
				indexImportedQuote(pageBlock, pageSize, uuid);
			}

			if(Objects.isNull(data) || DocumentType.agreement == data) {
				indexImportedAgreement(pageBlock, pageSize, uuid);
			}

			if(Objects.isNull(data) || DocumentType.opportunity == data) {
				indexImportedOpportunity(pageBlock, pageSize, uuid);
			}

			LOGGER.info("Indexing finished successfully. uuid: {}", uuid);
		} catch(ElasticsearchException ese) {
			LOGGER.error("Indexing failed. uuid: " + uuid, ese);
			if (MapUtils.isNotEmpty(ese.getFailedDocuments())) {
				StringBuilder sb = new StringBuilder();
				ese.getFailedDocuments().entrySet().forEach(entry->sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n"));
				LOGGER.error("See failed documents below. uuid: {} \n {}", uuid, sb);
			}
		} catch (Exception e) {
			LOGGER.error("Indexing failed. uuid: " + uuid, e);
		}
	}
	
	@Transactional
	public void indexImportedAccount(Integer pageBlock, Integer pageSize, String uuid) {
		List<IndexQuery> indexQueries = new ArrayList<>();
		
		LOGGER.info("Account indexing is started. uuid: {}", uuid);

		int startPage = 0;
		int endPage = pageBlock;
		List<AccountJson> accounts = new ArrayList<>();
		do {
			indexQueries.clear();
			accounts.clear();
			accounts.addAll(retrieveAccounts(startPage, endPage, pageSize));

			accounts.stream().forEach(account -> {
				CompanyLocation companyLocation = kartoteksServiceClient.getDefaultLocation(account.getCompany().getId());
				if(Objects.nonNull(companyLocation)) {
					account.setCity(companyLocation.getPostaladdress().getCity());
					account.setDistrict(companyLocation.getPostaladdress().getDistrict());					
				}
				indexQueries.add(new IndexQueryBuilder().withId(String.valueOf(account.getId())).withObject(AccountSearchDoc.fromAccount(account)).build());
				if(!CollectionUtils.isEmpty(account.getContacts())){
					account.getContacts().forEach(contact -> indexQueries.add(new IndexQueryBuilder().withId(String.valueOf(contact.getId())).withObject(ContactSearchDoc.fromContact(contact)).build()));
				}
			});

			if(!CollectionUtils.isEmpty(indexQueries)){
				elasticsearchTemplate.bulkIndex(indexQueries);
				LOGGER.info("Account indexed block {}-{}. uuid: {}", startPage, endPage, uuid);
			}
			startPage += pageBlock;
			endPage += pageBlock;
		} while(!accounts.isEmpty());
		LOGGER.info("Account indexing is finished. uuid: {}", uuid);
	}

	@Transactional
	public void indexImportedAgreement(Integer pageBlock, Integer pageSize, String uuid){
		List<IndexQuery> indexQueries = new ArrayList<>();

		LOGGER.info("Agreement indexing is started. uuid: {}", uuid);

		int startPage = 0;
		int endPage = pageBlock;
		List<AgreementJson> agreements = new ArrayList<>();
		do {
			indexQueries.clear();
			agreements.clear();
			agreements.addAll(retrieveAgreements(startPage, endPage, pageSize));

			agreements.stream().forEach(agreement -> indexQueries.add(new IndexQueryBuilder().withId(String.valueOf(agreement.getId())).withObject(AgreementSearchDoc.fromAgreement(agreement)).build()));

			if(!CollectionUtils.isEmpty(indexQueries)){
				elasticsearchTemplate.bulkIndex(indexQueries);
				LOGGER.info("Agreement indexed block {}-{}. uuid: {}", startPage, endPage, uuid);
			}
			startPage += pageBlock;
			endPage += pageBlock;
		} while(!agreements.isEmpty());
		LOGGER.info("Agreement indexing is finished. uuid: {}", uuid);
	}

	@Transactional
	public void indexImportedOpportunity(Integer pageBlock, Integer pageSize, String uuid){
		List<IndexQuery> indexQueries = new ArrayList<>();

		LOGGER.info("Opportunity indexing is started. uuid: {}", uuid);

		int startPage = 0;
		int endPage = pageBlock;
		List<OpportunityJson> opportunities = new ArrayList<>();
		do {
			indexQueries.clear();
			opportunities.clear();
			opportunities.addAll(retrieveOpportunities(startPage, endPage, pageSize));

			opportunities.stream().forEach(opportunity -> {
				opportunity.setAccountOwner(searchService.searchAccountOwner(opportunity.getAccount().getId()));
				indexQueries.add(new IndexQueryBuilder().withId(String.valueOf(opportunity.getId())).withObject(OpportunitySearchDoc.fromOpportunity(opportunity)).build());
			});

			if(!CollectionUtils.isEmpty(indexQueries)){
				elasticsearchTemplate.bulkIndex(indexQueries);
				LOGGER.info("Opportunity indexed block {}-{}. uuid: {}", startPage, endPage, uuid);
			}
			startPage += pageBlock;
			endPage += pageBlock;
		} while(!opportunities.isEmpty());
		LOGGER.info("Opportunity indexing is finished. uuid: {}", uuid);
	}
	
	@Transactional
	public void indexImportedQuote(Integer pageBlock, Integer pageSize, String uuid) {
		List<IndexQuery> indexQueries = new ArrayList<>();

		LOGGER.info("Quote indexing is started. uuid: {}", uuid);

		int startPage = 0;
		int endPage = pageBlock;
		List<QuoteJson> quotes = new ArrayList<>();
		do {
			indexQueries.clear();
			quotes.clear();
			quotes.addAll(retrieveQuotes(startPage, endPage, pageSize));

			quotes.stream().forEach(quote -> {
				quote.setAccountOwner(searchService.searchAccountOwner(quote.getAccount().getId()));
				indexQueries.add(new IndexQueryBuilder().withId(String.valueOf(quote.getId())).withObject(QuoteSearchDoc.fromQuote(quote)).build());
			});

			if(!CollectionUtils.isEmpty(indexQueries)){
				elasticsearchTemplate.bulkIndex(indexQueries);
				LOGGER.info("Quote indexed block {}-{}. uuid: {}", startPage, endPage, uuid);
			}
			startPage += pageBlock;
			endPage += pageBlock;
		} while(!quotes.isEmpty());
		LOGGER.info("Quote indexing is finished. uuid: {}", uuid);
	}
	
	public void indexAccount(Long accountId) {
		Optional.of(accountId)
		.map(crmAccountServiceClient::getAccountAsDetailed)
		.ifPresent(this::indexAccount);
	}

	@Transactional
	public void indexAccount(AccountJson account){
		createIndex();
		putType(DocumentType.account);
		CompanyLocation companyLocation = kartoteksServiceClient.getDefaultLocation(account.getCompany().getId());
		if(Objects.nonNull(companyLocation)) {
			account.setCity(companyLocation.getPostaladdress().getCity());
			account.setDistrict(companyLocation.getPostaladdress().getDistrict());					
		}
		elasticsearchTemplate.index(new IndexQueryBuilder().withId(account.getId().toString()).withObject(AccountSearchDoc.fromAccount(account)).build());
		elasticsearchTemplate.refresh(AccountSearchDoc.class);
		LOGGER.info("Account is indexed. id: {}", account.getId());
		updateAccountRelations(account);
		LOGGER.info("Account is indexing finished. id: {}", account.getId());

	}
	
	public void updateAccountRelations(AccountJson account) {
		SearchConfig query = new SearchConfig();
		MatchFilter filter = new MatchFilter();
		filter.setName(MatchType.ACCOUNT.getName());
		filter.setVal(String.valueOf(account.getId()));
		query.getMatchFilters().add(filter);
		Page<SearchDoc> documents = null;
		do {
			documents = searchService.searchQuoteAsPage(query, new DocumentType[] { DocumentType.agreement, DocumentType.quote, DocumentType.opportunity });
			for (SearchDoc document : documents.getContent()) {
				if (document instanceof QuoteSearchDoc) {
					indexQuote(QuoteSearchDoc.class.cast(document).getId());
				} else if (document instanceof AgreementSearchDoc) {
					indexAgreement(AgreementSearchDoc.class.cast(document).getId());
				} else if (document instanceof OpportunitySearchDoc) {
					indexOpportunity(OpportunitySearchDoc.class.cast(document).getId());
				}
			}
			query.nextPage();
		} while (documents.hasNext());
	}
	
	public void indexQuote(Long quoteId) {
		Optional.of(quoteId).map(crmQuoteServiceClient::getQuote).ifPresent(this::indexQuote);
	}

	@Transactional
	public void indexQuote(QuoteJson quote){
		createIndex();
		putType(DocumentType.quote);
		quote.setAccountOwner(searchService.searchAccountOwner(quote.getAccount().getId()));
		elasticsearchTemplate.index(new IndexQueryBuilder().withId(quote.getId().toString()).withObject(QuoteSearchDoc.fromQuote(quote)).build());
		LOGGER.info("Quote is indexed. id: {}", quote.getId());
	}
	
	public void deleteQuote(QuoteJson quote){
		deleteDocument(quote.getId(), DocumentType.quote);
	}

	@Transactional
	public void indexContact(ContactJson contact){
		createIndex();
		putType(DocumentType.contact);
		elasticsearchTemplate.index(new IndexQueryBuilder().withId(contact.getId().toString()).withObject(ContactSearchDoc.fromContact(contact)).build());
	}

	public void deleteContact(ContactJson contact){
		deleteDocument(contact.getId(), DocumentType.contact);
	}
	
	public void deleteAccount(AccountJson account) {
		deleteDocument(account.getId(), DocumentType.account);
	}

	public void indexAgreement(Long agreementId){
		Optional.of(agreementId)
				.map(agreementServiceClient::getAgreement)
				.ifPresent(this::indexAgreement);
	}

	@Transactional
	public void indexAgreement(AgreementJson agreement){
		createIndex();
		putType(DocumentType.agreement);
		elasticsearchTemplate.index(new IndexQueryBuilder().withId(agreement.getId().toString()).withObject(AgreementSearchDoc.fromAgreement(agreement)).build());
		LOGGER.info("Agreement is indexed. id: {}", agreement.getId());
	}

	public void deleteAgreement(AgreementJson agreement){
		deleteDocument(agreement.getId(), DocumentType.agreement);
	}

	public void indexOpportunity(Long opportunityId){
		Optional.of(opportunityId)
				.map(crmOpportunityServiceClient::getOpportunity)
				.ifPresent(this::indexOpportunity);
	}

	@Transactional
	public void indexOpportunity(OpportunityJson opportunity){
		createIndex();
		putType(DocumentType.opportunity);
		opportunity.setAccountOwner(searchService.searchAccountOwner(opportunity.getAccount().getId()));
		elasticsearchTemplate.index(new IndexQueryBuilder().withId(opportunity.getId().toString()).withObject(OpportunitySearchDoc.fromOpportunity(opportunity)).build());
		LOGGER.info("Opportunity is indexed. id: {}", opportunity.getId());
	}

	private List<AgreementJson> retrieveAgreements(Integer startPage, Integer endPage, Integer pageSize) {
		Integer page = startPage;
		boolean hasNext = true;
		List<AgreementJson> agreements = new ArrayList<>();
		ResponseEntity<RestResponsePage<AgreementJson>> response;
		while(hasNext && page < endPage){
			LOGGER.info("Retrieving agreements at page {}", page);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(agreementServiceName + "/agreement").queryParam("size", pageSize).queryParam("page", page);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<RestResponsePage<AgreementJson>>() {});
			agreements.addAll(response.getBody().getContent());
			hasNext = response.getBody().hasNext();
			page++;
		}
		return agreements;
	}

	private List<OpportunityJson> retrieveOpportunities(Integer startPage, Integer endPage, Integer pageSize) {
		Integer page = startPage;
		boolean hasNext = true;
		List<OpportunityJson> opportunities = new ArrayList<>();
		ResponseEntity<RestResponsePage<OpportunityJson>> response;
		while(hasNext && page < endPage){
			LOGGER.info("Retrieving opportunities at page {}", page);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(opportunityServiceName + "/opportunity").queryParam("size", pageSize).queryParam("page", page);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<RestResponsePage<OpportunityJson>>() {});
			opportunities.addAll(response.getBody().getContent());
			hasNext = response.getBody().hasNext();
			page++;
		}
		return opportunities;
	}

	private List<AccountJson> retrieveAccounts(Integer startPage, Integer endPage, Integer pageSize) {
		Integer page = startPage;
		boolean hasNext = true;
		List<AccountJson> accounts = new ArrayList<>();
		ResponseEntity<RestResponsePage<AccountJson>> response;
		while(hasNext && page < endPage){
			LOGGER.info("Retrieving accounts at page {}", page);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(crmAccountServiceName + "/account/detailed").queryParam("size", pageSize).queryParam("page", page);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<RestResponsePage<AccountJson>>() {});
			accounts.addAll(response.getBody().getContent());
			hasNext = response.getBody().hasNext();
			page++;
		}
		return accounts;
	}

	private List<QuoteJson> retrieveQuotes(Integer startPage, Integer endPage, Integer pageSize) {
		Integer page = startPage;
		boolean hasNext = true;
		List<QuoteJson> quotes = new ArrayList<>();
		ResponseEntity<RestResponsePage<QuoteJson>> response;

		while(hasNext && page < endPage){
			LOGGER.info("Retrieving quotes at page {}", page);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(crmQuoteServiceName + "/quote").queryParam("size", pageSize).queryParam("page", page);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<RestResponsePage<QuoteJson>>() {});
			quotes.addAll(response.getBody().getContent());
			hasNext = response.getBody().hasNext();
			page++;
		}
		return quotes;
	}
}
