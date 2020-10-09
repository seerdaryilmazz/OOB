package ekol.orders.transportOrder.service;

import ekol.exceptions.ApplicationException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ContractService {

    @Value("${oneorder.contract-service}")
    private String contractServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public IdNamePair findIdNamePairByIdOrThrowResourceNotFoundException(Long id) {
        try {
            return oAuth2RestTemplate.getForObject(contractServiceName + "/contract/id-and-name/" + id, IdNamePair.class);
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException httpClientErrorException = (HttpClientErrorException) e;
                if (httpClientErrorException.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    throw new ResourceNotFoundException("Contract with specified id cannot be found: " + id);
                }
            }
            throw new ApplicationException("Exception while contract query", e);
        }
    }
}
