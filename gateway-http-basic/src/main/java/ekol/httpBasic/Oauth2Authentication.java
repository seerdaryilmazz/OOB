package ekol.httpBasic;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;

import ekol.exceptions.ApplicationException;

@Component
public class Oauth2Authentication {
	
	@Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    public Map<String, String> authenticateOauth2(Authentication authentication){
    	return authenticateOauth2(authentication.getPrincipal(), authentication.getCredentials(), clientId, clientSecret);
    }
    public Map<String, String> authenticateOauth2(Object principal, Object credentials){
    	return authenticateOauth2(principal, credentials, clientId, clientSecret);
    	
    }
	public Map<String, String> authenticateOauth2(Object principal, Object credentials, String clientId, String clientSecret){
        ResponseEntity<Map> response = null;
        try {
            response = new RestTemplate().exchange(accessTokenUri, HttpMethod.POST,
                    new HttpEntity<>(createBody(principal, credentials, clientId), createHttpHeaders(clientId, clientSecret)), Map.class);

        }catch(HttpClientErrorException e){
        	throw e;
        }catch(Exception e){
        	throw new ApplicationException("Error from integration: " + e.getMessage());
        }
        if (response != null && response.getStatusCode() != HttpStatus.OK) {
            throw new ApplicationException("Error from integration with status code: " + response.getStatusCode());
        }
        return response.getBody();
    }
    private MultiValueMap<String, String> createBody(Object principal, Object credentials, String clientId){
        MultiValueMap<String, String> params = new HttpHeaders();
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("username", (String)principal);
        params.add("password", (String)credentials);
        return params;
    }
    private MultiValueMap<String, String> createHttpHeaders(String clientId, String clientSecret){
        MultiValueMap<String, String> headers = new HttpHeaders();
        String authHeader = "Basic " + new String(Base64.encodeBase64((clientId + ":" + clientSecret).getBytes(StandardCharsets.US_ASCII)));
        headers.add("Authorization", authHeader);
        return headers;

    }
	
}
