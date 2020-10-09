package ekol.location.client;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.location.client.dto.*;

@Component
public class KartoteksServiceClient {
	@Value("${oneorder.kartoteks-service}")
	private String url;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Company getCompany(Long companyId) {
		return restTemplate.getForObject(url+"/company/{companyId}", Company.class, companyId);
	}

	public CompanyLocation getCompanyLocation(Long companyLocationId) {
		return restTemplate.getForObject(url+"/location/{companyLocationId}", CompanyLocation.class, companyLocationId);
	}
}
