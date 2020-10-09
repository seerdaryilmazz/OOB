package ekol.crm.quote.domain.dto.sales;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.LookupValueLabel;
import lombok.*;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesDemandJson {

    private String id;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private LookupValueLabel currency;

}
