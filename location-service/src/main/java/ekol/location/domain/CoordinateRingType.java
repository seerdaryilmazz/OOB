package ekol.location.domain;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup
public enum CoordinateRingType {
    OUTER, INNER
}
