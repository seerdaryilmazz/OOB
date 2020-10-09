package ekol.crm.quote.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum IncotermExplanation {
    DOOR_TO_DOOR, DOOR_TO_PORT, PORT_TO_DOOR, PORT_TO_PORT
}
