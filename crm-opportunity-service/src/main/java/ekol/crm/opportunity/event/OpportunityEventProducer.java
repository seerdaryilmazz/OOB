package ekol.crm.opportunity.event;

import ekol.crm.opportunity.domain.dto.OpportunityJson;
import ekol.event.annotation.ProducesEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created by Dogukan Sahinturk on 22.11.2019
 */
@Component
public class OpportunityEventProducer {

    @ProducesEvent(event = "opportunity-update")
    @TransactionalEventListener(fallbackExecution = true, condition = "#event.operation == T(ekol.crm.opportunity.event.Operation).UPDATED")
    public OpportunityJson produceUpdateOpportunity(OpportunityEventData event){
        return OpportunityJson.fromEntity(event.getOpportunity());
    }
}
