package ekol.crm.inbound.client;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import ekol.crm.inbound.domain.dto.Account;
import ekol.model.IdNamePair;

@Component
public class AccountServiceClient {
	
	@Value("${oneorder.crm-account-service}")
	private String accountServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public IdNamePair findAccountByCompanyId(Long companyId) {
		return restTemplate.getForObject(accountServiceUrl + "/account/byCompany?companyId={companyId}", IdNamePair.class, companyId);
	}
	
	public IdNamePair findAccountByCompanyIdIgnoreException(Long companyId) {
		IdNamePair account = null;
		try {
			account = restTemplate.getForObject(accountServiceUrl + "/account/byCompany?companyId={companyId}", IdNamePair.class, companyId);
		} catch(HttpClientErrorException e) {
			if (HttpStatus.NOT_FOUND != e.getStatusCode()) {
				return null;
			}
		} 
		return account;
	}
	
	public Account findAccount(Long id) {
		return restTemplate.getForObject(accountServiceUrl + "/account/{companyId}", Account.class, id);
	}
}
