package ekol.orders.transportOrder.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by burak on 09/03/17.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum OrderPlanningOperationType {
    COLLECTION,DISTRIBUTION
}

