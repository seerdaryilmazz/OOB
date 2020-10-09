package ekol.crm.quote.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum VehicleType {
	
	TRAILER,
	TRUCK,
	LIGHT_TRUCK,
	VAN,
	;

}
