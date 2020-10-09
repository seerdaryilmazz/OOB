package ekol.crm.search.event.dto.opportunity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Dogukan Sahinturk on 27.01.2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class MonetaryAmountJson {
    private BigDecimal amount;
    private CodeNamePair currency;
}
