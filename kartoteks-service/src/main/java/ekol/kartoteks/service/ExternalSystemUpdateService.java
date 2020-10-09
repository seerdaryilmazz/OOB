package ekol.kartoteks.service;

import ekol.kartoteks.domain.exchange.QueueExchangeData;
import ekol.kartoteks.domain.export.connection.ExternalSystemConnectionProperties;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * Created by kilimci on 06/06/16.
 */
@Service
public class ExternalSystemUpdateService {

    public void postToExternalServer(QueueExchangeData exchangeData, ExternalSystemConnectionProperties properties){

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ExternalServerErrorHandler());
        restTemplate.exchange(properties.getEndpoint(), HttpMethod.POST,
                    new HttpEntity<>(exchangeData, createHttpHeaders(properties)), String.class);

    }
    private MultiValueMap<String, String> createHttpHeaders(ExternalSystemConnectionProperties properties){
        MultiValueMap<String, String> headers = new HttpHeaders();
        String authHeader = "Basic " + new String(Base64.encodeBase64((properties.getUsername() + ":" + properties.getPassword()).getBytes(Charset.forName("US-ASCII"))));
        headers.add("Authorization", authHeader);
        return headers;

    }
}
