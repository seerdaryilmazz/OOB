package ekol.orders.order.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum CompanyType {
	COMPANY,
	CUSTOMS,
	;
}
