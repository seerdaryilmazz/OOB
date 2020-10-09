package ekol.orders.order.service;

import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.response.kartoteks.CompanyResponse;
import ekol.orders.order.domain.dto.response.kartoteks.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class KartoteksServiceClient {

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksService;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    public KartoteksServiceClient(OAuth2RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public boolean isCompanyExists(Long companyId){
        return restTemplate.getForEntity(kartoteksService + "/company/" + companyId, IdNamePair.class)
                .getStatusCode().is2xxSuccessful();

    }

    public CompanyResponse getCompany(Long companyId){
        return restTemplate.getForObject(kartoteksService + "/company/" + companyId, CompanyResponse.class);

    }

    public boolean isLocationExists(Long locationId){
        return restTemplate.getForEntity(kartoteksService + "/location/" + locationId, IdNamePair.class)
                .getStatusCode().is2xxSuccessful();

    }

    public LocationResponse getLocation(Long locationId){
        return restTemplate.getForObject(kartoteksService + "/location/" + locationId, LocationResponse.class);

    }

    public boolean isContactExists(Long contactId){
        return restTemplate.getForEntity(kartoteksService + "/contact/" + contactId, IdNamePair.class)
                .getStatusCode().is2xxSuccessful();

    }
    public boolean isCompanyPartner(long companyId){
        return restTemplate.getForObject(kartoteksService + "/company/" + companyId + "/is-partner", Boolean.class);
    }
}
