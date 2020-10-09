package ekol.orders.order.domain.dto.json.updateOrder;

import ekol.model.CodeNamePair;
import ekol.orders.order.domain.AmountWithCurrency;

import java.math.BigDecimal;

public class AmountWithCurrencyJson {

    private BigDecimal amount;
    private CodeNamePair currency;

    public AmountWithCurrency toEntity(){
        if(getAmount() == null){
            return null;
        }
        AmountWithCurrency amountWithCurrency = new AmountWithCurrency();
        amountWithCurrency.setAmount(getAmount());
        if(getCurrency() != null){
            amountWithCurrency.setCurrency(getCurrency().getCode());
        }
        return amountWithCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CodeNamePair getCurrency() {
        return currency;
    }

    public void setCurrency(CodeNamePair currency) {
        this.currency = currency;
    }
}
