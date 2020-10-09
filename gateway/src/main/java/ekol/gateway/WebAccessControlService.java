package ekol.gateway;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

@Service
public class WebAccessControlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAccessControlService.class);

    @Value("${oneorder.authorization-service}")
    private String authorizationServiceName;

    @Value("${oneorder.gateway.authorize-ui:false}")
    private boolean gatewayAuthorizeUi;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    
    public boolean checkAccess(Authentication authentication, HttpServletRequest request) {
    	String url = request.getRequestURI();
        // Login olmuş kullanıcının ilgili adrese erişim yetkisi var mı?
    	Boolean currentUserHasPermissionToUrl = false;
    	if(gatewayAuthorizeUi && new AntPathMatcher("/").match("/ui/**", url)){
    		currentUserHasPermissionToUrl = oAuth2RestTemplate.getForObject(authorizationServiceName + "/authorizeUi?url={requestURI}", Boolean.class, url);
    	} else {
    		currentUserHasPermissionToUrl = oAuth2RestTemplate.getForObject(authorizationServiceName + "/authorize?url={requestURI}&method={method}", Boolean.class, url, request.getMethod());
    	}
    	
    	if(LOGGER.isDebugEnabled()) {
    		LOGGER.debug("Has user, {}, permission to '{}'? -> {}", authentication.getPrincipal(), request.getRequestURI(), currentUserHasPermissionToUrl);
    	}

        return currentUserHasPermissionToUrl;
    }

}
