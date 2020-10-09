package ekol.currency.util;

import ekol.json.serializers.common.ConverterType;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class Constants {

    public static final int FIRST_PAGE_NUMBER = 0;

    public static final int DEFAULT_PAGE_SIZE = 50;

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final Set<String> ALL_CURRENCIES_FOR_CENTRAL_BANK_OF_HUNGARY = Util.hashSet(
            "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK", "IDR", "ILS", "INR", "ISK", "JPY", "KRW",
            "MXN", "MYR", "NOK", "NZD", "PHP", "PLN", "RON", "RSD", "RUB", "SEK", "SGD", "THB", "TRY", "UAH", "USD", "ZAR");

    public static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    
    public static final int DEFAULT_READ_TIMEOUT = 10000;
    
    public static final ZoneId DEFAULT_ZONE_ID_FOR_HUNGARY = ZoneId.of("Europe/Budapest");
    public static final ZoneId DEFAULT_ZONE_ID_FOR_POLAND = ZoneId.of("Europe/Warsaw");
    public static final ZoneId DEFAULT_ZONE_ID_FOR_TURKEY = ZoneId.of("Europe/Istanbul");
    public static final ZoneId DEFAULT_ZONE_ID_FOR_EUROPE = ZoneId.of("UTC");
    
    public static final ConverterType CONVERTER_TYPE_FOR_PUBLISHER_ENUM = ConverterType.INITIAL_CASE;

}
