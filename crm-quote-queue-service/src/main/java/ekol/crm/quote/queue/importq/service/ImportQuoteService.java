package ekol.crm.quote.queue.importq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.quote.queue.common.dto.QuoteJson;
import ekol.crm.quote.queue.common.service.client.QuoteServiceClient;
import ekol.crm.quote.queue.importq.domain.ImportQuoteQueueItem;
import ekol.crm.quote.queue.importq.enums.Status;
import ekol.crm.quote.queue.importq.repository.QuoteImportQueueRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ImportQuoteService {
	
	private QuoteImportQueueRepository quoteImportQueueRepository;
	private QuoteServiceClient quoteServiceClient;

	public ImportQuoteQueueItem updateWithQuoteInfo(String id) {
		ImportQuoteQueueItem item = quoteImportQueueRepository.findOne(id);
		try {
			QuoteJson quote = quoteServiceClient.updateIdMapping(item);
			item.setQuoteNumber(quote.getNumber());
			item.setStatus(Status.SUCCESSFUL);
		} catch(Exception e) {
			item.setStatus(Status.FAILED);
		}
		return quoteImportQueueRepository.save(item);
	}
}
