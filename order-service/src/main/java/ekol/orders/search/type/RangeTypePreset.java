package ekol.orders.search.type;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.time.DateUtils;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName="of")
public class RangeTypePreset {

	private static final DecimalFormat NAME_FORMAT = new DecimalFormat("+#;-#");
	private static final DateFormat DATE_VALUE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	@NonNull
	private final String name;

	@NonNull
	private final String minimumRange;

	@NonNull
	private final String maximumRange;

	private boolean selected = false;

	public static List<RangeTypePreset> generate(RangeFilterType filterType, RangeTypePresetDefinition def) {
		List<RangeTypePreset> result = new ArrayList<>();
		if (def.isExistBeforeStart()) {
			result.add(build(filterType, Integer.MIN_VALUE, def.getStart() - def.getGap()));
		}
		for (int i = def.getStart(); i <= def.getEnd(); i += def.getGap()) {
			RangeTypePreset obj = build(filterType, 0, i);
			if (Objects.nonNull(obj)) {
				result.add(obj);
			}
		}
		if (def.isExistAfterStart()) {
			result.add(build(filterType, def.getStart(), Integer.MAX_VALUE));
		}
		return result;
	}

	private static RangeTypePreset build(RangeFilterType filterType, int min, int max) {
		String name = null;
		String minimumRange = null;
		String maximumRange = null;
		
		boolean existBeforeStart = Integer.MIN_VALUE == min;
		boolean existAfterEnd = Integer.MAX_VALUE == max;

		if (RangeFilterType.DATE == filterType) {
			Date init = Calendar.getInstance().getTime();

			if(existBeforeStart) {
				name = "<<";
			} else if(existAfterEnd) {
				name = ">>";
			} else {
				name = min == max && 0 == min ? "Today" : NAME_FORMAT.format(max);
			}
			minimumRange = existBeforeStart ? "" : DATE_VALUE_FORMAT.format(DateUtils.addDays(init, min));
			maximumRange = existAfterEnd ? "" : DATE_VALUE_FORMAT.format(DateUtils.addDays(init, max)) ;

		} else if (RangeFilterType.NUMERIC == filterType) {
			if(existBeforeStart) {
				name = "<<";
			} else if(existAfterEnd) {
				name = ">>";
			} else {
				name = NAME_FORMAT.format(max);
			}
			minimumRange = existBeforeStart ? "" : String.valueOf(min);
			maximumRange = existAfterEnd ? "" : String.valueOf(max);
		} else {
			return null;
		}
		return RangeTypePreset.of(name, minimumRange, maximumRange);
	}

	@Getter
	@Builder
	public static class RangeTypePresetDefinition {
		private boolean existBeforeStart;
		private boolean existAfterStart;
		private final int start;
		private final int end;
		private final int gap;
	}
}
