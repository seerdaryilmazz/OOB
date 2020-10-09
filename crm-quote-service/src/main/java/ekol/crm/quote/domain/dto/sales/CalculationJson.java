package ekol.crm.quote.domain.dto.sales;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.dto.PriceCalculationJson;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CalculationJson {
    private String priceCurrency;
    private BigDecimal minPrice;
    private BigDecimal basePrice;
    private BigDecimal calculatedPrice;
    private List<CalculationDiscountJson> discounts = new ArrayList<>();
    private String description;
    private SalesDemandJson campaign;

        public PriceCalculationJson toCrmPriceCalculation(){
        return PriceCalculationJson.builder()
                .minAmount(getMinPrice())
                .calculatedAmount(getCalculatedPrice())
                .availableDiscounts(!CollectionUtils.isEmpty(getDiscounts()) ? getDiscounts().stream().map(CalculationDiscountJson::toCrmPriceDiscount).collect(Collectors.toList()) : null)
                .build();
    }
}

