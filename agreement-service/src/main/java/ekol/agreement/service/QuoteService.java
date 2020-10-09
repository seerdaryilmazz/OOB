package ekol.agreement.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.agreement.domain.dto.agreement.BillingItemJson;
import ekol.agreement.util.Utils;

@Component
public class QuoteService {

	@Value("${oneorder.crm-quote-service}")
    private String quoteServiceName;
	
	@Autowired
    private RestTemplate restTemplate;

    public BillingItemJson getBillingItemByName(String name, boolean ignoreNotFoundException){
        return Utils.getForObject(ignoreNotFoundException, restTemplate, quoteServiceName + "/lookup/billing-item/by?name={name}" , BillingItemJson.class, name);
    }
}
