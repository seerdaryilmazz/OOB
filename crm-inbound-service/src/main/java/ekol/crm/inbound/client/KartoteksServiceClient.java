package ekol.crm.inbound.client;

import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.model.*;

@Component
public class KartoteksServiceClient {
	
	@Value("${oneorder.kartoteks-service}")
	private String kartoteksServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	public Set<IdNamePair> findCompanyByEmail(String email) {
		return Stream.of(restTemplate.getForObject(kartoteksServiceUrl+"/company/list-by-email?address={email}", IdNamePair[].class, email)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	public Set<IdNamePair> findCompanyByDomain(String address) {
		return Stream.of(restTemplate.getForObject(kartoteksServiceUrl+"/company/list-by-domain?address={address}", IdNamePair[].class, address)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	public Set<CodeNamePair> listServiceAreas() {
		return Stream.of(restTemplate.getForObject(kartoteksServiceUrl+"/business-segment-type", CodeNamePair[].class)).collect(Collectors.toCollection(LinkedHashSet::new));
	}

}
