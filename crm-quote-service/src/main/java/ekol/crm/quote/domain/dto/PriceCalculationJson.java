package ekol.crm.quote.domain.dto;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.model.PriceCalculation;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceCalculationJson {

    private BigDecimal minAmount;
    private BigDecimal proposedAmount;
    private BigDecimal calculatedAmount;
    private List<PriceDiscountJson> selectedDiscounts;
    private List<PriceDiscountJson> availableDiscounts;

    public PriceCalculation toEntity(){
        return PriceCalculation.builder()
                .minAmount(getMinAmount())
                .calculatedAmount(getCalculatedAmount())
                .discounts(Optional.ofNullable(getSelectedDiscounts()).orElseGet(Collections::emptyList).stream().map(PriceDiscountJson::toEntity).collect(Collectors.toList()))
                .build();
    }

    public static PriceCalculationJson fromEntity(PriceCalculation priceCalculation){
        return PriceCalculationJson.builder()
                .minAmount(priceCalculation.getMinAmount())
                .proposedAmount(priceCalculation.getCalculatedAmount())
                .calculatedAmount(priceCalculation.getCalculatedAmount())
                .selectedDiscounts(Optional.ofNullable(priceCalculation.getDiscounts()).orElseGet(Collections::emptyList).stream().map(PriceDiscountJson::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
