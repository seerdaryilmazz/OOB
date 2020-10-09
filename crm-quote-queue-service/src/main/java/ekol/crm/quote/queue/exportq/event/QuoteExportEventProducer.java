package ekol.crm.quote.queue.exportq.event;

import ekol.crm.quote.queue.exportq.domain.ExportQuoteQueueItem;
import ekol.event.annotation.ProducesEvent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class QuoteExportEventProducer {

	@ProducesEvent(event = "quote-export")
    @TransactionalEventListener(fallbackExecution = true, condition="#item.retryCount == 0")
    public QuoteExportEventMessage produce(ExportQuoteQueueItem item) {
		return QuoteExportEventMessage.with(item.getId());
    }
}
