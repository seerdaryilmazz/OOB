package ekol.crm.quote.domain.dto.sales;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.dto.PriceDiscountJson;
import lombok.*;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CalculationDiscountJson {
    private Long id;
    private String name;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;

    public PriceDiscountJson toCrmPriceDiscount(){
        return PriceDiscountJson.builder()
                .salesPriceId(getId())
                .name(getName())
                .amount(getDiscountAmount())
                .percentage(getDiscountRate()).build();
    }
}