package ekol.crm.account.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum CountryPointType {
    POSTAL, PORT, AIRPORT
}
