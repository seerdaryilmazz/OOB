package ekol.kartoteks.service;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.kartoteks.domain.dto.AccountJson;

@Service
public class CrmAccountService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CrmAccountService.class);

    @Value("${oneorder.crm-account-service}")
    private String crmAccountServiceName;

    @Autowired
    private RestTemplate restTemplate;

    public boolean doesCompanyHaveAnAccount(Long companyId) {
    	try {
    		return Optional.ofNullable(restTemplate.getForObject(crmAccountServiceName + "/account/check-if-company-has-account?companyId={companyId}", Boolean.class, companyId))
    				.map(Boolean::booleanValue)
    				.orElse(false);
    	} catch (Exception e) {
    		LOGGER.warn("###### doesCompanyHaveAnAccount is suppressed", e);
    		return false;
    	}
    }

    public AccountJson findAccountByCompanyId(Long companyId) {
        List<String> params = Arrays.asList(
        		"companyId={companyId}", 
        		"throwExceptionIfNotFound=false"
        		);
        try {
        	return restTemplate.getForObject(crmAccountServiceName + "/account/byCompany?" + StringUtils.join(params, "&"), AccountJson.class, companyId);
        } catch(Exception e) {
        	LOGGER.warn("###### findAccountByCompanyId is suppressed", e);
        	return null;
        }
    }
}
