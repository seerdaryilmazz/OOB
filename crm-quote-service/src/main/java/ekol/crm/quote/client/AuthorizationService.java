package ekol.crm.quote.client;

import java.util.*;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.domain.dto.authorizationservice.*;
import ekol.crm.quote.domain.dto.kartoteksservice.*;
import ekol.crm.quote.util.Utils;
import ekol.model.IdNamePair;

@Service
public class AuthorizationService {

    @Value("${oneorder.authorization-service}")
    private String authorizationServiceName;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KartoteksService kartoteksService;

    public Subsidiary findSubsidiaryById(Long id, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, authorizationServiceName + "/subsidiary/{id}", Subsidiary.class, id);
    }
    
    public Country findCountryOfSubsidiary(Long id) {
    	return Optional.ofNullable(findSubsidiaryById(id, false))
    			.map(Subsidiary::getCompanies)
    			.map(Collection::stream)
    			.orElseGet(Stream::empty)
    			.findFirst()
    			.map(IdNamePair::getId)
    			.map(t->kartoteksService.findCompanyById(t, false))
    			.map(Company::getCountry)
    			.orElse(null);
    }

    public List<User> getUsers(String departmentName) {
        return Optional.ofNullable(restTemplate.getForObject(authorizationServiceName + "/auth/getUserList/{departmentName}", User[].class, departmentName))
        		.map(Arrays::asList)
        		.orElseGet(ArrayList::new);
    }
}
