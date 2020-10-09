package ekol.crm.quote.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.common.Constants;
import ekol.crm.quote.domain.dto.MonetaryAmountJson;
import ekol.crm.quote.domain.dto.kartoteksservice.Country;
import ekol.crm.quote.domain.dto.product.*;
import ekol.crm.quote.domain.enumaration.Currency;
import ekol.exceptions.*;

@Service
public class ExchangeRateService {

    @Value("${oneorder.currency-service}")
    private String currencyServiceName;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private AuthorizationService authorizationService;


    /**
     * Rounds half up - scale 6
     */
    public BigDecimal getExchangeRate(Currency fromCurrency, Long subsidiaryId) {

        if (fromCurrency == Currency.EUR) {
            return BigDecimal.ONE;
        } else {
            return getExchangeRate(fromCurrency, Currency.EUR, subsidiaryId);
        }
    }

    /**
     * Rounds half up - scale 6
     */
    public BigDecimal getExchangeRate(Currency fromCurrency, Currency toCurrency, Long subsidiaryId) {
        if (fromCurrency == toCurrency) {
            return BigDecimal.ONE;
        }
    	int callCount = 0;
    	do {
            List<String> params = new ArrayList<>();
            String countryIso = "TR";
            if(Objects.nonNull(subsidiaryId)) {
            	countryIso = Optional.ofNullable(subsidiaryId).map(authorizationService::findCountryOfSubsidiary).map(Country::getIso).orElse(countryIso);
            }
            if(Objects.equals(countryIso, "TR")) {
            	params.add("publisherCode=" + Constants.EXCHANGE_PUBLISHER_CENTRAL_BANK_OF_TURKEY);
            	params.add("preferredValueCode=" + (0 == callCount ? Constants.PREFERRED_VALUE_BANKNOTE_SELLING : Constants.FOREX_SELLING));
            	params.add("crossRateCurrencyCode=" + Currency.TRY.name());
            	
            } else {
            	params.add("publisherCode=" + Constants.EXCHANGE_PUBLISHER_CENTRAL_BANK_OF_EUROPE);
            }
            params.add("conversionDate=" + Constants.DEFAULT_DATE_FORMATTER.format(LocalDate.now()));
            params.add("fromCurrencyCode=" + fromCurrency.name());
            params.add("toCurrencyCode=" + toCurrency.name());
            

            String url = currencyServiceName + "/exchange-rate/convert?" + StringUtils.join(params, "&");
            
            try {
            	BigDecimal value = restTemplate.getForEntity(url, BigDecimal.class).getBody();
            	return value.setScale(6, BigDecimal.ROUND_HALF_UP);
            } catch(Exception e) {
            	callCount ++;
            }

        } while(callCount < 2);
    	throw new ResourceNotFoundException("No exchange rate is found {0}->{1}", fromCurrency.name(), toCurrency.name());
    }


    /**
     * Rounds half up - scale 2
     */

    public List<ProductJson> calculateBundledProductExchangeAmount(List<ProductJson> products, Long subsidiaryId){
    	
        final EnumMap<Currency, BigDecimal> exchangeRateMap = new EnumMap<>(Currency.class);
   
        products.stream().forEach(product -> {
            if(product instanceof BundledProductJson){
                BundledProductJson bundledProduct = (BundledProductJson)product;
               
				bundledProduct.setExpectedTurnoverExchanged(calculateExchangeAmount(bundledProduct.getExpectedTurnoverOriginal(), exchangeRateMap,subsidiaryId));
                bundledProduct.setPriceExchanged(calculateExchangeAmount(bundledProduct.getPriceOriginal(), exchangeRateMap, subsidiaryId));
            }else{
                throw new ValidationException("Invalid product type");
            }
        });
        return products;
    }

    private MonetaryAmountJson calculateExchangeAmount(MonetaryAmountJson originalAmount, Map<Currency, BigDecimal> exchangeRateMap, Long subsidiaryId){

        if(originalAmount == null || originalAmount.getAmount() == null){
            return null;
        }
        if(originalAmount.getCurrency() == null){
            throw new ValidationException("Original amount currency should not be empty for Product");
        }
        if(exchangeRateMap == null){
            exchangeRateMap = new EnumMap<>(Currency.class);
        }
        if(!exchangeRateMap.containsKey(originalAmount.getCurrency())){
        	exchangeRateMap.put(originalAmount.getCurrency(),
        			getExchangeRate(originalAmount.getCurrency(), subsidiaryId));
        }
        MonetaryAmountJson exchangeAmount = new MonetaryAmountJson();
        exchangeAmount.setAmount(originalAmount.getAmount()
                .multiply(exchangeRateMap.get(originalAmount.getCurrency()))
                .setScale(2, BigDecimal.ROUND_HALF_UP));
        exchangeAmount.setCurrency(Currency.EUR);
        return exchangeAmount;
    }
}
