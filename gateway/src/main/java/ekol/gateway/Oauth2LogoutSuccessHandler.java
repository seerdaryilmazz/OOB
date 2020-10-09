package ekol.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kilimci on 28/03/2017.
 */
@Component
public class Oauth2LogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Value("${security.logoutUri}")
    private String logoutUri;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(null);
        response.sendRedirect(logoutUri);
    }
}
