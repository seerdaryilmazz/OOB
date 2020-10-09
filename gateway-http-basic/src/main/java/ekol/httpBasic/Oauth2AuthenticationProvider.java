package ekol.httpBasic;

import java.util.*;

import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.*;

/**
 * Created by kilimci on 16/01/2018.
 */
@Component
@WebListener
public class Oauth2AuthenticationProvider extends RequestContextListener implements AuthenticationProvider {
	
	@Autowired
	private Oauth2Authentication oauth2Authentication;

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
    	String onBehalfOf = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("X-On-Behalf-Of");
        
    	UsernamePasswordAuthenticationToken token = null;
    	Map<String, String> authResponse = oauth2Authentication.authenticateOauth2(authentication);
        if(StringUtils.isBlank(onBehalfOf)) {
        	token = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null, new ArrayList<>());
        } else {
        	String clientId = (String)authentication.getPrincipal();
        	String clientSecret = (String)authentication.getCredentials();
        	authResponse = oauth2Authentication.authenticateOauth2(onBehalfOf, authentication.getPrincipal(), clientId, clientSecret);
        	token = new UsernamePasswordAuthenticationToken(onBehalfOf, null, new ArrayList<>());
        }
        token.setDetails(authResponse);
        return token;
    }

}