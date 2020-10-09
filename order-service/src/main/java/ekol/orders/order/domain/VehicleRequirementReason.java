package ekol.orders.order.domain;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup
public enum VehicleRequirementReason {
    BY_WAREHOUSE, BY_LOAD, BY_ORDER
}
