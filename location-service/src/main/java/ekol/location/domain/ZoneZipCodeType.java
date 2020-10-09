package ekol.location.domain;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

/**
 * Created by ozer on 14/12/16.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum ZoneZipCodeType {
    STARTS, EQUALS
}
