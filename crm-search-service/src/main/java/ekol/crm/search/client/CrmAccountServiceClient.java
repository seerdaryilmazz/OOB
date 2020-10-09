package ekol.crm.search.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.crm.search.event.dto.account.AccountJson;
import ekol.model.CodeNamePair;

@Component
public class CrmAccountServiceClient {
	
	@Value("${oneorder.crm-account-service}")
	private String url;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public AccountJson getAccountAsDetailed(Long accountId) {
		return restTemplate.getForObject(url + "/account/{accountId}/detailed", AccountJson.class, accountId);
	}
	
	public CodeNamePair getShipmentLoadingTypeByCode(String code) {
		if(StringUtils.isEmpty(code)) {
			return null;
		}
		ResponseEntity<CodeNamePair> response = restTemplate.getForEntity(url+"/lookup/shipment-loading-type/byCode?code={code}", CodeNamePair.class, code);
		if(HttpStatus.OK == response.getStatusCode()) {
			return response.getBody();
		}
		return null;
	}
}
