package ekol.kartoteks.event;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.event.annotation.ProducesEvent;
import ekol.kartoteks.domain.dto.QueueItemCompleteEvent;
import ekol.resource.oauth2.SessionOwner;

/**
 * Created by kilimci on 01/03/2017.
 */
@Component
public class ImportQueueItemCompletedEventProducer {

    @Autowired
    private SessionOwner sessionOwner;

    @ProducesEvent(event = "company-import-queue-item-completed")
    @TransactionalEventListener(fallbackExecution = true, condition = "#event.operation == T(ekol.kartoteks.event.CompanyImportQueueEvent$Operation).IMPORT_COMPLETED")
    public QueueItemCompleteEvent produceQueueItemCompleteEvent(CompanyImportQueueEvent event) {
        return QueueItemCompleteEvent.with(event.getData(), sessionOwner.getCurrentUser());
    }
}
