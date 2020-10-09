package ekol.orders.transportOrder.dto.rule;


import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum ApprovalWorkflow {
    SUPERVISOR, MANAGER
}
