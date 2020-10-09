package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculationJson {

    private BigDecimal minAmount;
    private BigDecimal proposedAmount;
    private BigDecimal calculatedAmount;
    private List<PriceDiscountJson> selectedDiscounts;
    private List<PriceDiscountJson> availableDiscounts;

}
