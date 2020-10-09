package ekol.orders.transportOrder.dto.rule;


import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by burak on 01/08/17.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum AssignmentPlanRuleType {
    COLLECTION, DISTRIBUTION, LINEHAUL
}
