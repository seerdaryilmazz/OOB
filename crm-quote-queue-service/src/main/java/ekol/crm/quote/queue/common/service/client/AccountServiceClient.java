package ekol.crm.quote.queue.common.service.client;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import ekol.crm.quote.queue.common.dto.AccountJson;
import ekol.model.IdCodeName;

@Service
public class AccountServiceClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceClient.class);

    @Value("${oneorder.crm-account-service}")
    private String accountService;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    public AccountJson find(Long accountId) {
    	AccountJson account = null;
    	try {
    		account = restTemplate.getForObject(accountService + "/account/{accountId}", AccountJson.class, accountId);
    	} catch(Exception e) {
    		LOGGER.error("Account fetch error",e);
    	}
        return account;
    }
    
    public IdCodeName findCountryPoint(Long countryPointId) {
    	return restTemplate.getForObject(accountService + "/lookup/country-point/{countryPointId}", IdCodeName.class, countryPointId);
    }
}
