package ekol.location.domain.location.enumeration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

/**
 * Created by burak on 01/02/17.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum WarehouseRampUsageType {
    WEST_EUROPE_TRADELANE, EAST_EUROPE_TRADELANE
}
