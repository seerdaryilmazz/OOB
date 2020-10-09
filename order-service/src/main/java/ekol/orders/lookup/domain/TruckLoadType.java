package ekol.orders.lookup.domain;


import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup
public enum TruckLoadType {

    FTL, // Full Truck Load
    LTL // Less Truck Load

}
