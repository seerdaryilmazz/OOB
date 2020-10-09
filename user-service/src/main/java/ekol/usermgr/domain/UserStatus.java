package ekol.usermgr.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by kilimci on 13/04/16.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum UserStatus {
    ACTIVE, DISABLED
}
