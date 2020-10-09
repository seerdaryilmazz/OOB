package ekol.agreement.queue.event;

import ekol.agreement.queue.domain.RainbowQueueItem;
import ekol.agreement.queue.wscbfunitprice.wsdl.WSCBFUNITPRICEInput;
import ekol.event.annotation.ProducesEvent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created by Dogukan Sahinturk on 2.10.2019
 */

@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class AgreementExportEventProducer {

    @ProducesEvent(event = "agreement-export")
    @TransactionalEventListener(fallbackExecution = true)
    public AgreementExportEventMessage produce(RainbowQueueItem item) {
        return AgreementExportEventMessage.with(item.getId());
    }
}
