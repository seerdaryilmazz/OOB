package ekol.crm.quote.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.quote.domain.dto.CalculatedPriceResultJson;
import ekol.crm.quote.domain.dto.quote.QuoteJson;
import ekol.crm.quote.domain.enumaration.QuoteType;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import ekol.oneorder.configuration.ConfigurationApi;
import ekol.oneorder.configuration.dto.BooleanOption;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PriceCalculationService {
	
	private static final String COST_BASED_SPOT_QUOTE_TARIFF = "COST_BASED_SPOT_QUOTE_TARIFF";
	private static final String SERVICE_AREA_ROAD = "ROAD";
	
	private ConfigurationApi configurationApi;
	private SalesPriceService salesPriceService;
	private CostBasedPricingService costBasedPricingService;
	
	public CalculatedPriceResultJson calculatePrices(QuoteJson quote) {
		if (QuoteType.SPOT != quote.getType() || !Objects.equals(SERVICE_AREA_ROAD, quote.getServiceArea().getCode())){
    		throw new ValidationException("Calculation operation can be made only for road spot quotes");
    	}
		if(isCostBasedPricingEnabled(quote.getSubsidiary())) {
			return CalculatedPriceResultJson.with(costBasedPricingService.calculatePrices(quote), COST_BASED_SPOT_QUOTE_TARIFF);
		}
		return CalculatedPriceResultJson.with(salesPriceService.calculatePrices(quote), "DEFAULT");
	}
	
	public void checkPriceValidity(String billingItemCode, Quote quote){
		if (QuoteType.SPOT != quote.getType() || !Objects.equals(SERVICE_AREA_ROAD, quote.getServiceArea())) {
        	return;
        }
		
		if(isCostBasedPricingEnabled(quote.getSubsidiary())) {
			costBasedPricingService.checkPriceValidity(billingItemCode, quote);
		} else {
			salesPriceService.checkPriceValidity(billingItemCode, quote);
		}
	}
	
	private boolean isCostBasedPricingEnabled(IdNamePair subsidiary) {
		return Optional.ofNullable(configurationApi.getBoolean(COST_BASED_SPOT_QUOTE_TARIFF, subsidiary.getId()))
				.map(BooleanOption::getValue)
				.orElse(Boolean.FALSE)
				.booleanValue();
	}
}
