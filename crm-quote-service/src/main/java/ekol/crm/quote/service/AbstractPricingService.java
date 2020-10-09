package ekol.crm.quote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ekol.crm.quote.client.ExchangeRateService;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.quote.*;
import ekol.exceptions.ValidationException;
import lombok.Getter;

@Getter
public abstract class AbstractPricingService {
	
	@Autowired
	private BillingItemService billingItemService;
	
	@Autowired
    private ExchangeRateService exchangeRateService;
	
	protected void adjustWillValidatedPrices(SpotQuote quote, List<Price> willValidated, Price candidate){
        BillingItem billingItem = billingItemService.getBillingItemByName(candidate.getBillingItem());
        if(isCalculationRequired(billingItem.getCode(), quote)){
            willValidated.add(candidate);
            if(candidate.getPriceCalculation() == null){
                throw new ValidationException("Parameters affecting price have changed, please press Calculate button for recalculation.",
                        billingItem.getDescription());
            }
        }else {
            if(candidate.getPriceCalculation() != null){
                throw new ValidationException("Parameters affecting price have changed, please press Calculate button for recalculation.",
                        billingItem.getDescription());
            }
        }
    }
	
	protected abstract boolean isCalculationRequired(String billingItemCode, Quote quote);
}
