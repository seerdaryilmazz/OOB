package ekol.event.monitoring;


import ekol.event.annotation.ConsumesWebEvent;
import ekol.event.annotation.ProducesEvent;
import ekol.event.component.BeansWithEventResolver;
import ekol.event.model.ConsumeRequest;
import ekol.event.model.Event;
import ekol.event.model.EventWithContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

/**
 * Created by kilimci on 24/11/2017.
 */
@Component
public class EventMonitoring {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private BeansWithEventResolver beansWithEventResolver;

    @Value("${oneorder.event-monitor-service}")
    private String eventMonitorService;

    @Value("${spring.application.name}")
    private String springApplicationName;

    public static final String registerEventName = "register-events";


    private static final Logger LOGGER = LoggerFactory.getLogger(EventMonitoring.class);

    public void saveProducedEvent(EventWithContext eventWithContext){
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            try{
                HttpEntity<Event> request = new HttpEntity<>(eventWithContext.getEvent());
                oAuth2RestTemplate.exchange(eventMonitorService + "/event/produce", HttpMethod.POST, request, Void.class);
            }catch(Exception e){
                LOGGER.error("error monitoring produced event", e);
                //ignore this exception, so that it won't stop processing
            }

        }
    }

    public void saveConsumedEvent(OAuth2RestTemplate oAuth2RestTemplate, EventWithContext eventWithContext, String exception){
		try{
			HttpEntity<ConsumeRequest> request = new HttpEntity<>(new ConsumeRequest(springApplicationName, eventWithContext.getEvent(), exception));
			oAuth2RestTemplate.exchange(eventMonitorService + "/event/consume", HttpMethod.POST, request, Void.class);
		}catch(Exception e){
			LOGGER.error("error monitoring consumed event", e);
			//ignore this exception, so that it won't stop processing
		}
    }

    @ProducesEvent(event = EventMonitoring.registerEventName)
    public RegisterEventMessage registerEvents(ApplicationContext applicationContext) {
        List<ConsumesWebEvent> consumedEvents = beansWithEventResolver.findConsumedEvents();
        List<ProducesEvent> producedEvents = beansWithEventResolver.findProducedEvents(applicationContext);
        return RegisterEventMessage.createWith(springApplicationName, producedEvents, consumedEvents);
    }


}
