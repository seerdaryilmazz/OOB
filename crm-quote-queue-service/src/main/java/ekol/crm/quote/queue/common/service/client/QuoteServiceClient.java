package ekol.crm.quote.queue.common.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import ekol.crm.quote.queue.common.dto.*;
import ekol.crm.quote.queue.importq.domain.ImportQuoteQueueItem;
import ekol.crm.quote.queue.importq.dto.ImportQuoteOrderJson;
import lombok.*;

@Service
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class QuoteServiceClient {

	@Value("${oneorder.crm-quote-service}")
    private String quoteService;

	@NonNull
    private OAuth2RestTemplate restTemplate;
    
    public QuoteJson getQuote(Long id) {
    	return restTemplate.getForObject(quoteService + "/quote/{id}", QuoteJson.class, id);
    }
    
    public QuoteJson updateIdMapping(ImportQuoteQueueItem item) {
    	return restTemplate.postForObject(quoteService + "/quote/id-mapping?_method=patch&number={quoteNumber}", QuoteIdMappingJson.with(item.getData().getExternalSystemName(), item.getData().getExternalSystemCode()) ,QuoteJson.class, item.getQuoteNumber());
    }
    
    public QuoteJson updateOrderMapping(ImportQuoteOrderJson orderInfo) {
    	return restTemplate.postForObject(quoteService + "/quote/order-mapping?_method=patch&number={quoteNumber}", orderInfo, QuoteJson.class, orderInfo.getQuoteCode());
    }
}
