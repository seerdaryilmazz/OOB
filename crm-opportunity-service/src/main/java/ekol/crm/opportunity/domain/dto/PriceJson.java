package ekol.crm.opportunity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.dto.MonetaryAmountJson;
import ekol.crm.opportunity.domain.enumaration.QuotePriceType;
import lombok.*;

/**
 * Created by Dogukan Sahinturk on 9.12.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PriceJson {
    private Long id;
    private QuotePriceType type;
    private MonetaryAmountJson charge;
    private MonetaryAmountJson priceExchanged;
}