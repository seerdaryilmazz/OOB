package ekol.crm.activity.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum CalendarStatus {
	UPCOMING,
	ONGOING,
	EXPIRED,
	;
}
