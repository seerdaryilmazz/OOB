package ekol.orders.transportOrder.service;


import ekol.orders.transportOrder.domain.Company;
import ekol.orders.transportOrder.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 09/08/2017.
 */
@Component
public class KartoteksClient {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksService;

    public Company findCompanyById(Long id) {
        return oAuth2RestTemplate.getForObject(kartoteksService + "/company/" + id, Company.class);
    }

    public Location findLocationById(Long id){
        return oAuth2RestTemplate.getForObject(kartoteksService + "/location/" + id, Location.class);
    }
}
