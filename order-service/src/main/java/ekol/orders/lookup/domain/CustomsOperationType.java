package ekol.orders.lookup.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum CustomsOperationType {
    BONDED_WAREHOUSE, //antrepo
    CUSTOMS_WAREHOUSE, //ambar
    CUSTOMER_CUSTOMS_WAREHOUSE, //fiktif
    CUSTOMS_CLEARANCE_PARK, //supalan
    FREE_ZONE,
    ON_BOARD_BONDED_WAREHOUSE, //araç üstü işlem
}
