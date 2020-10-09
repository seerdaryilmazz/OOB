package ekol.crm.opportunity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.model.MonetaryAmount;
import ekol.crm.opportunity.domain.enumaration.Currency;
import lombok.*;

import java.math.BigDecimal;

/**
 * Created by Dogukan Sahinturk on 18.11.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MonetaryAmountJson {
    private BigDecimal amount;
    private Currency currency;

    {
        this.amount = BigDecimal.ZERO;
        this.currency = Currency.EUR;
    }

    public MonetaryAmount toEntity(){
        return MonetaryAmount.builder()
                .amount(getAmount())
                .currency(getCurrency())
                .build();
    }

    public static MonetaryAmountJson fromEntity(MonetaryAmount monetaryAmount){
        if(monetaryAmount == null){
            return null;
        }
        return new MonetaryAmountJson.MonetaryAmountJsonBuilder()
                .amount(monetaryAmount.getAmount())
                .currency(monetaryAmount.getCurrency())
                .build();
    }

    public MonetaryAmountJson addAmount(BigDecimal addend) {
        this.amount = this.amount.add(addend);
        return this;
    }

    public MonetaryAmountJson subtractAmount(BigDecimal subtrahend){
        this.amount = this.amount.subtract(subtrahend);
        return this;
    }

    public static MonetaryAmountJson setInitialValues() {
        return new MonetaryAmountJson.MonetaryAmountJsonBuilder()
                .amount(BigDecimal.ZERO)
                .currency(Currency.EUR)
                .build();
    }
}
