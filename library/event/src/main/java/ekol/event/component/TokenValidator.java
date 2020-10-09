package ekol.event.component;

import ekol.event.model.EventWithContext;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by kilimci on 15/08/2017.
 */
@Component
public class TokenValidator {

    @Value("${security.oauth2.client.checkTokenUri}")
    private String checkTokenUri;

    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    private static final Logger LOG = LoggerFactory.getLogger(TokenValidator.class);

    private String[] refreshToken(String principal, String refreshToken) {
        LOG.info("Trying to refresh token");

        ResponseEntity<Map> response = new RestTemplate().exchange(
                accessTokenUri,
                HttpMethod.POST,
                new HttpEntity<>(
                        getMVM("grant_type", "refresh_token", "client_id", clientId, "username", principal, "refresh_token", refreshToken),
                        getAuthHeader()),
                Map.class);

        LOG.info("Acquired new access and refresh tokens");

        return new String[]{
                (String) response.getBody().get("access_token"),
                (String) response.getBody().get("refresh_token")
        };
    }
    private MultiValueMap<String, String> getMVM(String... values) {
        MultiValueMap<String, String> mvm = new HttpHeaders();
        for (int i = 0; i < values.length - 1; i += 2) {
            mvm.add(values[i], values[i + 1]);
        }
        return mvm;
    }

    private MultiValueMap<String, String> getAuthHeader() {
        return getMVM("Authorization", "Basic " + new String(Base64.encodeBase64((clientId + ":" + clientSecret).getBytes(Charset.forName("US-ASCII")))));
    }
    public void validateTokens(EventWithContext eventWithContext) {
        if (eventWithContext.getOauth2AccessToken() != null) {
            LOG.info("Will validate tokens");
            if (!isTokenValid(eventWithContext.getOauth2AccessToken().getValue())) {
                String[] newTokens = refreshToken(eventWithContext.getUser().getUsername(), eventWithContext.getOauth2AccessToken().getRefreshToken().getValue());
                LOG.info("Updating security context with new access and refresh tokens");
                eventWithContext.updateSecurityContext(newTokens[0], newTokens[1]);
            }
        }
    }
    private boolean isTokenValid(String value) {
        LOG.info("Checking token validity");
        try {
            new RestTemplate().exchange(
                    checkTokenUri,
                    HttpMethod.POST,
                    new HttpEntity<>(
                            getMVM("token", value),
                            getAuthHeader()),
                    Map.class);
            LOG.info("Token is valid");
            return true;
        } catch (Exception e) {
            LOG.info("Token is invalid");
            return false;
        }
    }
}
