package ekol.currency.domain;


import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.NONE)
public enum Currency {
    AUD,
    BGN,
    BRL,
    CAD,
    CHF,
    CLP,
    CNY,
    CZK,
    DKK,
    EUR,
    GBP,
    HKD,
    HRK,
    HUF,
    IDR,
    ILS,
    INR,
    IRR,
    ISK,
    JPY,
    KRW,
    KWD,
    MXN,
    MYR,
    NOK,
    NZD,
    PHP,
    PKR,
    PLN,
    QAR,
    RON,
    RSD,
    RUB,
    SAR,
    SEK,
    SGD,
    THB,
    TRY,
    UAH,
    USD,
    XDR,
    ZAR;
}
