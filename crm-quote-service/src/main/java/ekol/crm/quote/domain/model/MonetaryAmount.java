package ekol.crm.quote.domain.model;

import java.math.BigDecimal;
import java.util.Comparator;

import javax.persistence.*;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import ekol.crm.quote.domain.enumaration.Currency;
import ekol.exceptions.BadRequestException;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonetaryAmount implements Comparable<MonetaryAmount> {

    @Column
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;


    public MonetaryAmount addAmount(BigDecimal addend) {
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

    	if (!(object instanceof MonetaryAmount))
            return false;
        if (object == this)
            return true;

        MonetaryAmount node =  MonetaryAmount.class.cast(object);
        return new EqualsBuilder().
                append(getAmount(), node.getAmount()).
                append(getCurrency(), node.getCurrency()).
                isEquals();
    }

	@Override
	public int compareTo(MonetaryAmount o) {
		if(getCurrency() != o.getCurrency()) {
			throw new BadRequestException();
		}

		return Comparator
				.comparing(MonetaryAmount::getAmount)
				.compare(this, o);
	}
	
	public int compareTo(BigDecimal o) {
		return compareTo(new MonetaryAmount(o, getCurrency()));
	}
}
