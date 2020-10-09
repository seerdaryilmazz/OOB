package ekol.crm.search.client;

import java.text.MessageFormat;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import ekol.crm.search.domain.dto.CompanyLocation;

@Component
public class KartoteksServiceClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(KartoteksServiceClient.class); 
	
    @Value("${oneorder.kartoteks-service}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public CompanyLocation getDefaultLocation(Long companyId) {
    	try {
    		return restTemplate.getForObject(url + "/company/{companyId}/default-location", CompanyLocation.class, companyId);
    	} catch(RestClientResponseException ex) {
    		LOGGER.warn(MessageFormat.format("Default location fetch fault. ID: {0}", companyId), ex);
    	}
        return null;
    }
}
