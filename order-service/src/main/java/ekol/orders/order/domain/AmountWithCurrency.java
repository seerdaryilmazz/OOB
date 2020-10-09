package ekol.orders.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class AmountWithCurrency {

    @Column
    private BigDecimal amount;

    @Column(length = 3)
    private String currency;

    public static AmountWithCurrency with(BigDecimal amount, String currency){
        AmountWithCurrency amountWithCurrency = new AmountWithCurrency();
        amountWithCurrency.setAmount(amount);
        amountWithCurrency.setCurrency(currency);
        return amountWithCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
