package ekol.crm.quote.queue.importq.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.crm.quote.queue.importq.domain.ImportQuoteQueueItem;
import ekol.crm.quote.queue.importq.dto.ImportQuoteOrderJson;
import ekol.event.annotation.ProducesEvent;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ImportQuoteEventProducer {
	
	@ProducesEvent(event = "quote-import")
	@TransactionalEventListener(fallbackExecution = true)
	public ImportQuoteEventMessage produce(ImportQuoteQueueItem item) {
		return ImportQuoteEventMessage.with(item.getId(), item.getData());
	}
	
	@ProducesEvent(event = "quote-order-import")
	@TransactionalEventListener(fallbackExecution = true)
	public ImportQuoteOrderJson produce(ImportQuoteOrderEvent event) {
		return event.getData();
	}
}
