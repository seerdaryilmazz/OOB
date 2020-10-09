package ekol.location.domain.location.enumeration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

/**
 * Created by kilimci on 28/04/2017.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum TerminalRegistrationMethod {
    REGISTRATION_C, REGISTRATION_D
}
