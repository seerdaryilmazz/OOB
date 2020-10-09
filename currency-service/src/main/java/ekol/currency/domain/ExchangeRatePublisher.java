package ekol.currency.domain;


import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum ExchangeRatePublisher {
    CENTRAL_BANK_OF_HUNGARY, CENTRAL_BANK_OF_POLAND, CENTRAL_BANK_OF_TURKEY, CENTRAL_BANK_OF_EUROPE
}
