package ekol.orders.transportOrder.domain;


import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum OrderType {

    CONTRACTED, SPOT, NO_OFFER

}
