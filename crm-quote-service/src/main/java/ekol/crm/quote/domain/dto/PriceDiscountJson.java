package ekol.crm.quote.domain.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.model.PriceDiscount;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PriceDiscountJson {

    private Long id;
    private Long salesPriceId;
    private String name;
    private BigDecimal percentage;
    private BigDecimal amount;

    public PriceDiscount toEntity(){
        return PriceDiscount.builder()
                .id(getId())
                .salesPriceId(getSalesPriceId())
                .name(getName())
                .percentage(getPercentage())
                .amount(getAmount())
                .build();
    }

    public static PriceDiscountJson fromEntity(PriceDiscount discount){
        return PriceDiscountJson.builder()
                .id(discount.getId())
                .salesPriceId(discount.getSalesPriceId())
                .name(discount.getName())
                .percentage(discount.getPercentage())
                .amount(discount.getAmount())
                .build();
    }
    
}
