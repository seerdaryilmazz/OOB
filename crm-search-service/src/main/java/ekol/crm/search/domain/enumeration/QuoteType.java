package ekol.crm.search.domain.enumeration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum QuoteType {
    SPOT, LONG_TERM, TENDER;
}
