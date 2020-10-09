package ekol.crm.opportunity.event;

import ekol.crm.opportunity.domain.model.Opportunity;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Dogukan Sahinturk on 22.11.2019
 */
@Data
@AllArgsConstructor(staticName = "with")
public class OpportunityEventData {
    Opportunity opportunity;
    Operation operation;
}
