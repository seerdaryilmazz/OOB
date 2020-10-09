package ekol.orders.order.domain;


import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup
public enum Status {

    CREATED, CONFIRMED, APPROVED, REJECTED, PLANNED, COLLECTED, DELIVERED, CLOSED, CANCELLED

}
