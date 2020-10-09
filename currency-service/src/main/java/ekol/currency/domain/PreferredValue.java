package ekol.currency.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum PreferredValue {
    FOREX_BUYING, FOREX_SELLING, BANKNOTE_BUYING, BANKNOTE_SELLING
}
