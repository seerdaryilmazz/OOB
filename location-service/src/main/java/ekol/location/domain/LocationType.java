package ekol.location.domain;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum LocationType {
    CUSTOMER_WAREHOUSE, CROSSDOCK_WAREHOUSE, PORT, TRAIN_TERMINAL, PARKING_AREA, CUSTOMS
}
