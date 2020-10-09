package ekol.orders.lookup.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum ServiceType {

    STANDARD, EXPRESS, SUPER_EXPRESS, SPEEDY, SPEEDYXL

}
