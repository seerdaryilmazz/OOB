package ekol.hibernate5.domain.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BigDecimalRange implements Serializable {

    public static final int VALUE_IS_LESS_THAN_MIN = -1;
    public static final int VALUE_IS_IN_RANGE = 0;
    public static final int VALUE_IS_GREATER_THAN_MAX = 1;

    @Column
    private BigDecimal minValue;

    @Column
    private BigDecimal maxValue;

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public int check(BigDecimal value) {

        int result = VALUE_IS_IN_RANGE;

        if (minValue != null) {
            if (value.compareTo(minValue) == -1) {
                result = VALUE_IS_LESS_THAN_MIN;
            }
        }

        if (maxValue != null) {
            if (value.compareTo(maxValue) == 1) {
                result = VALUE_IS_GREATER_THAN_MAX;
            }
        }

        return result;
    }
}

