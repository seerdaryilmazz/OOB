package ekol.kartoteks.event;

import ekol.event.annotation.ProducesEvent;
import ekol.kartoteks.domain.CompanyImportQueue;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CompanyImportEventProducer {

    @ProducesEvent(event = "company-import")
    @TransactionalEventListener(fallbackExecution = true, condition = "#event.operation == T(ekol.kartoteks.event.CompanyImportQueueEvent$Operation).IMPORT")
    public CompanyImportEventMessage produce(CompanyImportQueueEvent event) {
        return CompanyImportEventMessage.createWithImportQueue(event.getData());
    }
}
