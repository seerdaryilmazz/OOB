package ekol.usermgr.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by ozer on 17/02/2017.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum UserAuthenticationType {
    ACTIVE_DIRECTORY, PASSWORD;
}
