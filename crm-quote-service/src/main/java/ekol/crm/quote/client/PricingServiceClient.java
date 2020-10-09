package ekol.crm.quote.client;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.domain.dto.pricingservice.*;


@Component
public class PricingServiceClient {
	
	@Value("${oneorder.pricing-service}")
	private String url;

	@Autowired
	private RestTemplate restTemplate;
	
	public CalculationResult[] calculate(Calculation request) {
		return restTemplate.postForObject(url + "/calculate", request, CalculationResult[].class);
	}
}
