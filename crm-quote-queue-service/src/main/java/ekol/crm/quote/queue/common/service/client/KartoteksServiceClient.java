package ekol.crm.quote.queue.common.service.client;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.queue.common.dto.CompanyJson;

@Component
public class KartoteksServiceClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KartoteksServiceClient.class);
	
	@Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

	@Autowired
    private RestTemplate restTemplate;
	
	public CompanyJson findCompanyById(Long id) {
		CompanyJson result = null;
		try {
			result = restTemplate.getForObject(kartoteksServiceName + "/company/{id}", CompanyJson.class, id);
		} catch(Exception e) {
			LOGGER.error("Company fetch error",e);
		}
		return result; 
	}
}
