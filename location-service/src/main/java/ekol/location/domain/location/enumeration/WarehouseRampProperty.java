package ekol.location.domain.location.enumeration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

/**
 * Created by burak on 01/02/17.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum WarehouseRampProperty {
    STANDARD_DOCK, SIDE_LOADING_DOCK, NOT_SUITABLE_FOR_CONTAINERS
}
