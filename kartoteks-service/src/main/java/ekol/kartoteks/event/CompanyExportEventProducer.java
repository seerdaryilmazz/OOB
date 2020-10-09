package ekol.kartoteks.event;

import ekol.event.annotation.ProducesEvent;
import ekol.kartoteks.domain.CompanyExportQueue;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created by kilimci on 07/12/2017.
 */
@Component
public class CompanyExportEventProducer {

    @ProducesEvent(event = "company-export")
    @TransactionalEventListener(fallbackExecution = true)
    public CompanyExportEventMessage produce(CompanyExportQueue exportQueue) {
        return CompanyExportEventMessage.createWithExportQueue(exportQueue);
    }

    @ProducesEvent(event = "company-retry-export", delay = 5 * 60 * 1000)
    public CompanyExportEventMessage produceDelayed(CompanyExportQueue exportQueue) {
        return CompanyExportEventMessage.createWithExportQueue(exportQueue);
    }
}
