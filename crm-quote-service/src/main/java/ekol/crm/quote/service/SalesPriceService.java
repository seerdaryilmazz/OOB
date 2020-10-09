package ekol.crm.quote.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.*;

import ekol.crm.quote.client.SalesPriceServiceClient;
import ekol.crm.quote.common.Constants;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.product.SpotProductJson;
import ekol.crm.quote.domain.dto.quote.*;
import ekol.crm.quote.domain.dto.sales.*;
import ekol.crm.quote.domain.dto.validation.*;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.enumaration.Currency;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.product.SpotProduct;
import ekol.crm.quote.domain.model.quote.*;
import ekol.crm.quote.util.ProductUtils;
import ekol.exceptions.ValidationException;
import ekol.model.*;

@Service
public class SalesPriceService extends AbstractPricingService {

    @Autowired
    private PriceValidationRequestConverter priceValidationRequestConverter;
    
    @Autowired
    private SalesPriceServiceClient salesPriceServiceClient;

    public CalculatedPriceJson[] calculatePrices(QuoteJson quote) {
    	SpotQuoteJson spotQuote = (SpotQuoteJson)quote;
    	List<PriceJson> prices = spotQuote.getPrices();

    	prices.forEach(price -> {
    		Set<Long> selectedDiscountIds = Optional.ofNullable(price.getPriceCalculation())
    				.map(PriceCalculationJson::getSelectedDiscounts)
    				.map(Collection::stream).orElseGet(Stream::empty)
    				.map(PriceDiscountJson::getSalesPriceId)
    				.collect(Collectors.toSet());
    		price.clear();
    		if(isCalculationRequired(price.getBillingItem().getCode(), spotQuote.toEntity())){
    			CalculationJson calculationDetails = calculateSalesPrice(price.getBillingItem(), spotQuote);
    			if(calculationDetails != null){
    				PriceCalculationJson priceCalculation = calculationDetails.toCrmPriceCalculation();
    				List<PriceDiscountJson> selectedDiscounts = Optional.ofNullable(priceCalculation.getAvailableDiscounts())
    						.map(Collection::stream)
    						.orElseGet(Stream::empty)
    						.filter(t->selectedDiscountIds.contains(t.getSalesPriceId()))
    						.collect(Collectors.toList());
    				
    				BigDecimal discountedAmount = Optional.ofNullable(selectedDiscounts)
							.map(Collection::stream)
							.orElseGet(Stream::empty)
							.map(PriceDiscountJson::getAmount)
							.reduce(priceCalculation.getCalculatedAmount(), (a1, a2)->a1.subtract(a2));
    				
    				priceCalculation.setCalculatedAmount(discountedAmount);
    				priceCalculation.setSelectedDiscounts(selectedDiscounts);
    				price.setPriceCalculation(priceCalculation);
    				price.setCampaign(calculationDetails.getCampaign());
    				price.setCampaignId(Optional.of(calculationDetails).map(CalculationJson::getCampaign).map(SalesDemandJson::getId).orElse(null));
    				price.setAuthorization(this.adjustPriceAuthorization(price));
    				
    				Currency currency = Currency.valueOf(calculationDetails.getPriceCurrency());
    				BigDecimal approvedAmount = Optional.of(price).map(PriceJson::getAuthorization).map(PriceAuthorizationJson::getApprovedAmount).map(MonetaryAmountJson::getAmount).orElse(discountedAmount);
    				
    				BigDecimal maxAmount = Stream.of(approvedAmount, discountedAmount).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    				BigDecimal minAmount = Stream.of(approvedAmount, discountedAmount).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    				
    				BigDecimal amount = Stream.of(maxAmount, minAmount).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    				BigDecimal exchangeRate = getExchangeRateService().getExchangeRate(currency, quote.getSubsidiary().getId());
    				BigDecimal exchangePrice = amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);

    				price.setCharge(new MonetaryAmountJson(amount, currency));
    				price.setPriceExchanged(new MonetaryAmountJson(exchangePrice, Currency.EUR));
    			}
    		}
    	});
		return new CalculatedPriceJson[] { CalculatedPriceJson.with(IdCodeName.with(null, "DEFAULT", "DEFAULT"), prices) };
    }
    
    private PriceAuthorizationJson adjustPriceAuthorization(PriceJson price) {
    	Currency currency = price.getCharge().getCurrency();
    	PriceAuthorizationJson auth = price.getAuthorization();
    	if(Objects.isNull(auth) || Objects.isNull(price.getPriceCalculation())) {
    		return null;
    	}
    	if(0 == new MonetaryAmountJson(BigDecimal.ZERO, currency).compareTo(new MonetaryAmountJson(price.getPriceCalculation().getCalculatedAmount(),currency))) {
    		return null;
    	}
    	if(0 != auth.getCalculatedAmount().compareTo(price.getPriceCalculation().getCalculatedAmount())) {
    		return null;
    	}
    	if(0 != auth.getMinimumAmount().compareTo(price.getPriceCalculation().getMinAmount())) {
    		return null;
    	}
    	return auth;
    }

    private CalculationJson calculateSalesPrice(BillingItem billingItem, SpotQuoteJson quote) {

        SpotProductJson product = (SpotProductJson) quote.getProducts().iterator().next();

        if("ROAD_FREIGHT".equals(billingItem.getName())){
            String arrivalCountry = product.getToCountry().getIso();
            String arrivalPostalCode = product.getToPoint().getName();
            if(ProductUtils.isImport(product)) {
            	CustomsPointJson customsArrival = quote.getCustoms().getArrival();
            	if ("CUSTOMER_ADDRESS".equals(product.getDeliveryType().getCode())
            			&& Stream.of("CUSTOMS_CLEARANCE_PARK", "FREE_ZONE", "CUSTOMER_CUSTOMS_WAREHOUSE").noneMatch(customsArrival.getClearanceType().getCode()::equals)
            			&& Objects.nonNull(customsArrival.getLocation())) {
            		arrivalCountry = Optional.ofNullable(customsArrival.getCustomsLocationCountry()).map(IsoNamePair::getIso).orElse(arrivalCountry);
            		arrivalPostalCode = Optional.ofNullable(customsArrival.getCustomsLocationPostal()).map(IdNamePair::getName).orElse(arrivalPostalCode);
            	}
            }
            return salesPriceServiceClient.calculateFrieght(
            		product.getFromCountry().getIso(), 
            		product.getFromPoint().getName(), 
            		arrivalCountry, 
            		arrivalPostalCode, 
            		product.getShipmentLoadingType(), 
            		quote.getPayWeight()
            		);
        }else{
        	return salesPriceServiceClient.calculateSurcharge(
        			product.getFromCountry().getIso(),
        			product.getFromPoint().getName(),
        			product.getToCountry().getIso(),
        			product.getToPoint().getName(),
        			billingItem.getCode()
        			);
        }
    }

    public void checkPriceValidity(String billingItemCode, Quote quote) {
        SpotQuote spotQuote = SpotQuote.class.cast(quote);
        List<Price> willValidated = new ArrayList<>();
        if(StringUtils.isEmpty(billingItemCode)){
            spotQuote.getPrices().forEach(price -> adjustWillValidatedPrices(spotQuote, willValidated, price));
        }else{
            spotQuote.getPrices().stream()
            	.filter(p->Objects.equals(billingItemCode, getBillingItemService().getBillingItemByName(p.getBillingItem()).getCode()))
            	.findFirst().ifPresent(price->adjustWillValidatedPrices(spotQuote, willValidated, price));
        }
        if(!CollectionUtils.isEmpty(willValidated)){
        	ValidationJson response = salesPriceServiceClient.validateSalesPrice(priceValidationRequestConverter.convert(spotQuote, willValidated));
        	if(!response.isResult()) {
                throw new ValidationException(response.getDescription());
            }
        }
    }
    
    protected boolean isCalculationRequired(String billingItemCode, Quote quote) {
        if (QuoteType.SPOT == quote.getType()) {
            if (Constants.FREIGHT_BILLING_ITEM_CODE.equals(billingItemCode)) {
                return eligibleForFreightCalculation(SpotQuote.class.cast(quote));
            }
            return true;
        }
        return false;
    }
	
	protected boolean eligibleForFreightCalculation(SpotQuote spotQuote){
        if (CollectionUtils.isEmpty(spotQuote.getProducts()) || spotQuote.getProducts().size() != 1) {
            throw new ValidationException("Quote should have one product");
        }
        SpotProduct product = (SpotProduct)spotQuote.getProducts().iterator().next();
        if (!Constants.COUNTRY_CODE_TR.equals(product.getFromCountry().getIso()) && !Constants.COUNTRY_CODE_TR.equals(product.getToCountry().getIso())) {
            return false;
        }
        if(!isPricingCustomsOfficeExists(spotQuote.getCustoms())){
            return false;
        }
        if (!CollectionUtils.isEmpty(spotQuote.getLoads())) {
            return spotQuote.getLoads().stream().noneMatch(load ->
                    (load.getType() == LoadType.FRIGO || load.getType() == LoadType.ROAD_OVERSIZE));
        }
        return true;
    }
	
	protected boolean isPricingCustomsOfficeExists(Customs customs){

        Set<Long> customsOfficeIds = new HashSet<>();

        Optional.ofNullable(customs)
        		.map(Customs::getDeparture)
                .map(CustomsPoint::getOffice)
                .map(IdNamePair::getId)
                .ifPresent(customsOfficeIds::add);

        Optional.ofNullable(customs)
        		.map(Customs::getArrival)
                .map(CustomsPoint::getOffice)
                .map(IdNamePair::getId)
                .ifPresent(customsOfficeIds::add);

        if(CollectionUtils.isEmpty(customsOfficeIds)) {
        	throw new ValidationException("Customs office should be selected");
        }
        return !Stream.of(salesPriceServiceClient.listPricingCustomsOffice())
        		.map(PricingCustomsOfficeJson::getCustomsOffice)
        		.map(IdNamePair::getId)
        		.noneMatch(customsOfficeIds::contains);
    }
}
