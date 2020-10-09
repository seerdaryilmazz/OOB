package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PriceJson {

    private Long id;
    private CodeNamePair type;
    private BillingItemJson billingItem;
    private boolean addToFreight;
    private MonetaryAmountJson charge;
    private MonetaryAmountJson priceExchanged;
    private PriceCalculationJson priceCalculation;

}
