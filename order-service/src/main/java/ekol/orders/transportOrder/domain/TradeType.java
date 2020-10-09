package ekol.orders.transportOrder.domain;


import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup
public enum TradeType {

    NONE, IMPORT, EXPORT, EU, OTHER

}
