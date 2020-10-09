package ekol.authorization.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.service.AuthorizationEventConsumerService;
import ekol.authorization.service.InitService;
import ekol.event.annotation.ConsumesWebEvent;
import ekol.event.auth.AuthorizationEvent;

/**
 * Created by kilimci on 04/12/2017.
 */
@RestController
@RequestMapping("/event-consumer")
public class EventConsumerController {
	
	@Value("${spring.application.name}")
	private  String applicationName;

	@Autowired
    private AuthorizationEventConsumerService authorizationEventConsumerService;

    @PostMapping(value = "/authorization")
    @ConsumesWebEvent(event = "authorization", name = "saves operations and permissions")
    public void consumeOrderCreatedEvent(@RequestBody AuthorizationEvent eventMessage) {
        authorizationEventConsumerService.consumeAuthorizationEvent(eventMessage);
    }
}
