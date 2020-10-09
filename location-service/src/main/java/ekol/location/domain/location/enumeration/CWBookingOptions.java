package ekol.location.domain.location.enumeration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum CWBookingOptions {
    NEVER, ASK, ALWAYS
}