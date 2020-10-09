package ekol.location.domain;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum RouteLegType {
    ROAD, RAILWAY, SEAWAY
}
