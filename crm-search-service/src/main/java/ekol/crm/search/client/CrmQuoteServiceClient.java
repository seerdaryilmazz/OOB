package ekol.crm.search.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.crm.search.event.dto.quote.QuoteJson;

@Component
public class CrmQuoteServiceClient {
	
	@Value("${oneorder.crm-quote-service}")
	private String url;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public QuoteJson getQuote(Long quoteId) {
		return restTemplate.getForObject(url + "/quote/{quoteId}", QuoteJson.class, quoteId);
	}
	
}
