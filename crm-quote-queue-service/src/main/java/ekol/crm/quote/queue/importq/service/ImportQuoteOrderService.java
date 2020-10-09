package ekol.crm.quote.queue.importq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ekol.crm.quote.queue.common.service.client.QuoteServiceClient;
import ekol.crm.quote.queue.importq.dto.ImportQuoteOrderJson;
import ekol.crm.quote.queue.importq.event.ImportQuoteOrderEvent;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ImportQuoteOrderService {
	
	private QuoteServiceClient quoteServiceClient;
	private ApplicationEventPublisher applicationEventPublisher;
	
	public String importQuoteOrderData(ImportQuoteOrderJson data) {
		applicationEventPublisher.publishEvent(ImportQuoteOrderEvent.with(data));
		return HttpStatus.OK.name();
	}

	public void updateOrderMapping(ImportQuoteOrderJson data) {
		quoteServiceClient.updateOrderMapping(data);
	}
}
