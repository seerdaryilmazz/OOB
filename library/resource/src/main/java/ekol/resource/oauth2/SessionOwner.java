package ekol.resource.oauth2;

import ekol.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.stereotype.Component;

/**
 * Created by ozer on 04/10/16.
 */
@Component
public class SessionOwner {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public static User getUserFromAccessToken(String accessToken) {
        return User.createFromJwtMap(
                JsonParserFactory.create().parseMap(
                        JwtHelper.decode(accessToken).getClaims()));
    }

    public User getCurrentUser() {
        return getUserFromAccessToken(oAuth2RestTemplate.getAccessToken().getValue());
    }
}
