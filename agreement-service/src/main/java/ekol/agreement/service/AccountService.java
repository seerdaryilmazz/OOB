package ekol.agreement.service;

import ekol.agreement.domain.dto.Account;
import ekol.agreement.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Dogukan Sahinturk on 11.10.2019
 */
@Service
public class AccountService {

	@Value("${oneorder.crm-account-service}")
    private String accountServiceName;

	@Autowired
    private RestTemplate restTemplate;

    public Account findAccountById(Long id, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, accountServiceName + "/account/{id}", Account.class, id);
    }
}
