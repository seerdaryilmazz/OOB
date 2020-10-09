package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.BigDecimalRange;

import java.math.BigDecimal;

public final class BigDecimalRangeBuilder {
    private BigDecimal minValue;
    private BigDecimal maxValue;

    private BigDecimalRangeBuilder() {
    }

    public static BigDecimalRangeBuilder aBigDecimalRange() {
        return new BigDecimalRangeBuilder();
    }

    public BigDecimalRangeBuilder withMinValue(BigDecimal minValue) {
        this.minValue = minValue;
        return this;
    }

    public BigDecimalRangeBuilder withMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public BigDecimalRange build() {
        BigDecimalRange bigDecimalRange = new BigDecimalRange();
        bigDecimalRange.setMinValue(minValue);
        bigDecimalRange.setMaxValue(maxValue);
        return bigDecimalRange;
    }
}
