package ekol.event.auth;


import ekol.event.component.EventApplicationReadyListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Created by ozer on 28/10/16.
 */
@Component
public class AuthorizationApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    public static final int ORDER = EventApplicationReadyListener.ORDER + 100;

    @Autowired
    private AuthorizationRegisterer authorizationRegisterer;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        authorizationRegisterer.registerAuthorizations();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
