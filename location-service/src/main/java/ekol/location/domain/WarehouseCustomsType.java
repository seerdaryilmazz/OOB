package ekol.location.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum WarehouseCustomsType {
    NON_BONDED_WAREHOUSE, //gümrüksüz
    BONDED_WAREHOUSE, //antrepo
    CUSTOMS_WAREHOUSE, //ambar
    CUSTOMER_CUSTOMS_WAREHOUSE, //fiktif
    CUSTOMS_CLEARANCE_PARK, //supalan
    EUROPE_CUSTOMS_LOCATION,

}
