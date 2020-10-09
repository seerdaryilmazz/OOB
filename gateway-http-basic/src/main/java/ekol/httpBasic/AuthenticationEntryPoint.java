package ekol.httpBasic;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 16/01/2018.
 */
@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("One Order");
        super.afterPropertiesSet();
    }

}