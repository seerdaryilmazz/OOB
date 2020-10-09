package ekol.kartoteks.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.dto.EoriVerifyServiceResponse;
import ekol.kartoteks.domain.dto.IdNamePair;
import ekol.kartoteks.domain.dto.TaxofficeVerifyServiceResponse;
import ekol.kartoteks.repository.CompanyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthorizationService {

    @Value("${oneorder.authorization-service}")
    private String authorizationServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public List<IdNamePair> findOperationsOfCurrentUser() {
        String url = authorizationServiceName + "/auth/operation/my";
        ResponseEntity<List<IdNamePair>> response = oAuth2RestTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<IdNamePair>>() {});
        return response.getBody();
    }

}
