package ekol.crm.quote.domain.dto;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.enumaration.Currency;
import ekol.crm.quote.domain.model.MonetaryAmount;
import ekol.exceptions.BadRequestException;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonetaryAmountJson implements Comparable<MonetaryAmountJson> {
    private BigDecimal amount = BigDecimal.ZERO;
    private Currency currency = Currency.EUR;

    public MonetaryAmount toEntity(){
        return new MonetaryAmount(getAmount(), getCurrency());
    }

    public static MonetaryAmountJson fromEntity(MonetaryAmount monetaryAmount){
    	return Optional.ofNullable(monetaryAmount).map(t->new MonetaryAmountJson(monetaryAmount.getAmount(), monetaryAmount.getCurrency())).orElse(null);
    }

    public MonetaryAmountJson addAmount(BigDecimal addend) {
        this.amount = this.amount.add(addend);
        return this;
    }
    
    public int hashCode() {
    	return new HashCodeBuilder()
    			.append(getAmount())
    			.append(getCurrency())
    			.toHashCode();
    }
    
    public boolean equals(Object object) {

    	if (!(object instanceof MonetaryAmountJson))
            return false;
        if (object == this)
            return true;

        MonetaryAmountJson node =  MonetaryAmountJson.class.cast(object);
        return new EqualsBuilder().
                append(getAmount(), node.getAmount()).
                append(getCurrency(), node.getCurrency()).
                isEquals();
    }

	@Override
	public int compareTo(MonetaryAmountJson o) {
		if(getCurrency() != o.getCurrency()) {
			throw new BadRequestException();
		}

		return Comparator
				.comparing(MonetaryAmountJson::getAmount)
				.compare(this, o);
	}
	
	public int compareTo(BigDecimal o) {
		return compareTo(new MonetaryAmountJson(o, getCurrency()));
	}
}
