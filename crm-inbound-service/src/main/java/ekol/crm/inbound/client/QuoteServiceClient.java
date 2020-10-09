package ekol.crm.inbound.client;

import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.crm.inbound.domain.dto.Document;
import ekol.model.CodeNamePair;

@Component
public class QuoteServiceClient {
	
	@Value("${oneorder.crm-quote-service}")
	private String quoteServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Set<CodeNamePair> listQuoteTypes() {
		return Stream.of(restTemplate.getForObject(quoteServiceUrl + "/lookup/quote-type", CodeNamePair[].class)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	public List<Document> updateDocuments(Long quoteId, List<Document> documents) {
		return Stream.of(restTemplate.exchange(quoteServiceUrl + "/quote/{quoteId}/documents", HttpMethod.PUT, new HttpEntity<>(documents), Document[].class, quoteId).getBody()).collect(Collectors.toList());
	}
}
