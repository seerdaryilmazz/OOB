package ekol.httpBasic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;

@Service
public class WebAccessControlService {

    @Value("${oneorder.authorization-service}")
    private String authorizationServiceName;

    @Autowired
    private RestTemplate restTemplate;

    public boolean checkAccess(Authentication authentication, HttpServletRequest request) {
        Boolean result = restTemplate.exchange(buildUri(request),
                HttpMethod.GET, buildHttpEntity(authentication), Boolean.class).getBody();
        return result;
    }
    private URI buildUri(HttpServletRequest request){
        String url = authorizationServiceName + "/authorize";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("url", request.getRequestURI())
                .queryParam("method", request.getMethod());
        return builder.build().encode().toUri();
    }
    private HttpEntity<?> buildHttpEntity(Authentication authentication){
        Map<String, String> details = (Map<String, String>)authentication.getDetails();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Authorization", details.get("token_type") + " " + details.get("access_token"));

        return new HttpEntity<>(headers);
    }

}
