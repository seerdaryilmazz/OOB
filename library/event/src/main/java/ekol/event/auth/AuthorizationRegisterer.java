package ekol.event.auth;


import ekol.event.annotation.ProducesEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ozer on 27/02/2017.
 */
@Component
public class AuthorizationRegisterer {

    public static final String authorizationEvent = "authorization";

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @ProducesEvent(event = authorizationEvent)
    public ekol.event.auth.AuthorizationEvent registerAuthorizations() {
        ekol.event.auth.AuthorizationEvent authorizationEvent = new AuthorizationEvent();
        authorizationEvent.setServiceName(springApplicationName);

        handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            List<String> operations = new ArrayList<>();
            if (handlerMethod.getMethod().isAnnotationPresent(Authorize.class)) {
                Authorize authorize = handlerMethod.getMethodAnnotation(Authorize.class);
                operations = Arrays.asList(authorize.operations());
            }
            authorizationEvent.addAuthorizationEventItem(
                    requestMappingInfo.getPatternsCondition().getPatterns(),
                    operations,
                    requestMappingInfo.getMethodsCondition().getMethods());
        });

        return authorizationEvent;
    }
}
