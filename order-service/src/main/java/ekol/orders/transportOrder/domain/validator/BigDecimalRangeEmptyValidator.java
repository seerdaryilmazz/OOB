package ekol.orders.transportOrder.domain.validator;

import java.util.Objects;

import ekol.hibernate5.domain.embeddable.BigDecimalRange;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class BigDecimalRangeEmptyValidator {

	public static boolean isEmpty(BigDecimalRange bigDecimalRange) {
		return Objects.isNull(bigDecimalRange) 
				|| (Objects.isNull(bigDecimalRange.getMinValue()) && Objects.isNull(bigDecimalRange.getMaxValue()));
	}
	public static boolean isNotEmpty(BigDecimalRange bigDecimalRange) {
		return !isEmpty(bigDecimalRange);
	}
}
