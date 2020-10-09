package ekol.crm.opportunity.domain.model;

import ekol.crm.opportunity.domain.enumaration.Currency;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

/**
 * Created by Dogukan Sahinturk on 14.11.2019
 */
@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonetaryAmount {

    @Column
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;


    public MonetaryAmount addAmount(BigDecimal addend) {
        this.amount = this.amount.add(addend);
        return this;
    }

    public static MonetaryAmount createDefault(){
        return new MonetaryAmount(BigDecimal.ZERO, Currency.EUR);
    }
}

