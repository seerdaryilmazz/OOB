package ekol.crm.quote.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ekol.crm.quote.domain.dto.sales.*;
import ekol.crm.quote.domain.dto.validation.*;

@Component
public class SalesPriceServiceClient {
	
	@Value("${oneorder.sales-price-service}")
    protected String salesPriceServiceName;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public CalculationJson calculateFrieght(
			String departureCountry, 
			String departurePostalCode,
			String arrivalCountry,
			String arrivalPostalCode,
			String truckLoadType,
			BigDecimal payweight) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(salesPriceServiceName + "/calculate/no-log")
                .queryParam("departureCountry", departureCountry)
                .queryParam("departurePostalCode", departurePostalCode)
                .queryParam("arrivalCountry", arrivalCountry)
                .queryParam("arrivalPostalCode", arrivalPostalCode)
                .queryParam("truckLoadType", truckLoadType)
                .queryParam("payWeight", payweight)
                .queryParam("searchCampaign", true);
		return restTemplate.getForObject(builder.build().encode().toUri(), CalculationJson.class);
	}
	
	public CalculationJson calculateSurcharge(
			String departureCountry, 
			String departurePostalCode,
			String arrivalCountry,
			String arrivalPostalCode,
			String billingItemCode) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(salesPriceServiceName + "/billing-item-price/calculate")
                .queryParam("departureCountry", departureCountry)
                .queryParam("departurePostalCode", departurePostalCode)
                .queryParam("arrivalCountry", arrivalCountry)
                .queryParam("arrivalPostalCode", arrivalPostalCode)
                .queryParam("billingItemCode", billingItemCode);
		return restTemplate.getForObject(builder.build().encode().toUri(), CalculationJson.class);
	}
	
	public PricingCustomsOfficeJson[] listPricingCustomsOffice() {
		return restTemplate.getForObject(salesPriceServiceName + "/pricing-customs-office", PricingCustomsOfficeJson[].class);
	}
	
	public ValidationJson validateSalesPrice(PriceValidationRequest request) {
		request.validate();
		return restTemplate.postForObject(salesPriceServiceName + "/validate/price", request, ValidationJson.class);
	}
}
