package ekol.event.component;

import ekol.event.monitoring.EventMonitoring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Created by ozer on 28/10/16.
 */
@Component
public class EventApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    public static final int ORDER = Ordered.LOWEST_PRECEDENCE;

    @Autowired
    private WebEventRegisterer webEventRegisterer;

    @Autowired
    private EventMonitoring eventMonitoring;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        eventMonitoring.registerEvents(event.getApplicationContext());
        webEventRegisterer.registerConsumers(event.getApplicationContext());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
