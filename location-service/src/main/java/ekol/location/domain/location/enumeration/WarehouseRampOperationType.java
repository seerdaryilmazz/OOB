package ekol.location.domain.location.enumeration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

/**
 * Created by burak on 16/02/17.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum WarehouseRampOperationType {
    GOODS_IN, GOODS_OUT, GOODS_IN_AND_GOODS_OUT;
}
