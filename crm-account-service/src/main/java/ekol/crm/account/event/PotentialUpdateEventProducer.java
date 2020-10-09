package ekol.crm.account.event;


import ekol.crm.account.domain.dto.potential.PotentialJson;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.event.annotation.ProducesEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PotentialUpdateEventProducer {

    @ProducesEvent(event = "potential-update")
    @TransactionalEventListener(fallbackExecution = true)
    public PotentialJson produce(Potential potential) {
        return potential.toJson();
    }
}
