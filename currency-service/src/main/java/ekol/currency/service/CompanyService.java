package ekol.currency.service;

import ekol.currency.domain.dto.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public Company findByIdOrThrowResourceNotFoundException(Long id) {
        return oAuth2RestTemplate.getForObject(kartoteksServiceName + "/company/" + id, Company.class);
    }
}
