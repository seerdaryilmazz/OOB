package ekol.crm.quote.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.UPPER_CASE)
public enum Currency {
    EUR, USD, TRY, GBP, RON;
}
