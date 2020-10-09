package ekol.authorization.service;

import ekol.authorization.dto.Company;
import ekol.authorization.dto.IdNamePair;
import ekol.exceptions.ApplicationException;
import ekol.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;
import java.util.List;

@Service
public class CompanyService {

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public Company findByIdOrThrowResourceNotFoundException(Long id) {
        return oAuth2RestTemplate.getForObject(kartoteksServiceName + "/company/" + id, Company.class);
    }

    public List<IdNamePair> findIdNamePairsOrThrowResourceNotFoundException(Collection<Long> ids) {
        String url = kartoteksServiceName + "/company/" + StringUtils.join(ids, ",") + "/id-and-name";
        ResponseEntity<List<IdNamePair>> response = oAuth2RestTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<IdNamePair>>() {});
        return response.getBody();
    }

    public IdNamePair findIdNamePairByIdOrThrowResourceNotFoundException(Long id) {
        String url = kartoteksServiceName + "/company/" + id + "/id-and-name";
        ResponseEntity<List<IdNamePair>> response = oAuth2RestTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<IdNamePair>>() {});
        return response.getBody().get(0);
    }
}
