package ekol.orders.order.service;

import ekol.orders.order.domain.dto.response.SubsidiaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationServiceClient {

    @Value("${oneorder.authorization-service}")
    private String service;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    public AuthorizationServiceClient(OAuth2RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    public SubsidiaryResponse getSubsidiary(Long subsidiaryId){
        return restTemplate.getForObject(service + "/subsidiary/" + subsidiaryId, SubsidiaryResponse.class);
    }

}
