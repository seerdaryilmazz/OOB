package ekol.location.domain;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum LinehaulRouteFragmentType {
    ONE_LEGGED, MULTIPLE_LEGGED
}
