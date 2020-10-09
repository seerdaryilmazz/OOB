package ekol.crm.opportunity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Dogukan Sahinturk on 9.12.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class QuoteProductJson {
    private Long id;
    private MonetaryAmountJson expectedTurnoverOriginal;
    private CodeNamePair status;
    private MonetaryAmountJson expectedTurnoverExchanged;

}
