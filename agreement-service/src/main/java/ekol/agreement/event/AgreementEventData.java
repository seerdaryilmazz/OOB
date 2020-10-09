package ekol.agreement.event;

import ekol.agreement.domain.model.agreement.Agreement;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "with")
public class AgreementEventData {
    private Agreement agreement;
    private Operation operation;
}
