package ekol.crm.quote.common;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String FREIGHT_BILLING_ITEM_CODE = "1101";
    public static final String COUNTRY_CODE_TR = "TR";
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final String EXCHANGE_PUBLISHER_CENTRAL_BANK_OF_TURKEY = "CENTRAL_BANK_OF_TURKEY";
    public static final String PREFERRED_VALUE_BANKNOTE_SELLING = "BANKNOTE_SELLING";
    public static final String INCOTERM_EXP_DOOR_TO_PORT = "DOOR_TO_PORT";
    public static final String INCOTERM_EXP_PORT_TO_PORT = "PORT_TO_PORT";
    public static final BigDecimal LDM_COEFFICIENT = new BigDecimal(1750);
    public static final BigDecimal LDM_COEFFICIENT_SPEEDY = new BigDecimal(700);
    public static final BigDecimal VOLUME_COEFFICIENT = new BigDecimal(333);
    public static final BigDecimal VOLUME_COEFFICIENT_SPEEDY = new BigDecimal(150);
    public static final String SHIPMENT_LOADING_TYPE_FTL = "FTL";
    public static final String SHIPMENT_LOADING_TYPE_LTL = "LTL";
    public static final String STANGE_PACKAGE_TYPE_CODE = "Stange";
    public static final BigDecimal FTL_TOTAL_LDM = new BigDecimal("13.6");
    public static final BigDecimal FTL_TOTAL_LDM_SPEEDY = new BigDecimal("6.0");
    public static final BigDecimal FTL_TOTAL_LDM_SPEEDY_VAN= new BigDecimal("4.0");
    public static final String SERVICE_TYPE_CATEGORY_MAIN = "MAIN";
    public static final String SERVICE_TYPE_CATEGORY_EXTRA = "EXTRA";
    public static final String EXCHANGE_PUBLISHER_CENTRAL_BANK_OF_EUROPE = "CENTRAL_BANK_OF_EUROPE";
    public static final String FOREX_SELLING = "FOREX_SELLING";
    
    public static final String ARRIVAL = "ARRIVAL";
    public static final String DEPARTURE = "DEPARTURE";
    
    public static final String IMPORT = "IMPORT";
    public static final String EXPORT = "EXPORT";
    public static final String NON_TURKEY = "NON_TURKEY";
    
    public static final String EUROPE_CUSTOMS_LOCATION = "EUROPE_CUSTOMS_LOCATION";
    
    public static final String WAREHOUSE = "WAREHOUSE";
    public static final String BONDED_WAREHOUSE = "BONDED_WAREHOUSE";
    
    public static final String BILLING_ITEM_DELIVERY_COST_DOT = "DELIVERY_COST_DOT";
    
}
