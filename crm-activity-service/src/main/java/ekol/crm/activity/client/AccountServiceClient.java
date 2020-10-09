package ekol.crm.activity.client;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import ekol.crm.activity.client.dto.Account;

@Component
public class AccountServiceClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceClient.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${oneorder.crm-account-service}")
	private String accountService;
	
	public Account getAccount(Long id) {
		try {
			return restTemplate.getForObject(accountService + "/account/{id}", Account.class, id);
		}catch(HttpClientErrorException e){
			LOGGER.error("Account not found. id: {}", id);
			throw e;
		}
	}
}
