package ekol.orders.order.builder;

import ekol.orders.order.domain.AmountWithCurrency;

import java.math.BigDecimal;

public final class AmountWithCurrencyBuilder {
    private BigDecimal amount;
    private String currency;

    private AmountWithCurrencyBuilder() {
    }

    public static AmountWithCurrencyBuilder anAmountWithCurrency() {
        return new AmountWithCurrencyBuilder();
    }

    public AmountWithCurrencyBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AmountWithCurrencyBuilder withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public AmountWithCurrencyBuilder but() {
        return anAmountWithCurrency().withAmount(amount).withCurrency(currency);
    }

    public AmountWithCurrency build() {
        AmountWithCurrency amountWithCurrency = new AmountWithCurrency();
        amountWithCurrency.setAmount(amount);
        amountWithCurrency.setCurrency(currency);
        return amountWithCurrency;
    }
}
