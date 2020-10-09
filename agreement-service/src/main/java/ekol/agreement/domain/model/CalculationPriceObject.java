package ekol.agreement.domain.model;

import ekol.agreement.domain.dto.agreement.HistoryUnitPriceJson;
import ekol.agreement.domain.dto.agreement.UnitPriceJson;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculationPriceObject {

    private UnitPriceJson unitPrice;
    private HistoryUnitPriceJson historyUnitPrice;
}
