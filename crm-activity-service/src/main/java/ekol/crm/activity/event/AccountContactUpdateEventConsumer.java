package ekol.crm.activity.event;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.activity.event.dto.Contact;
import ekol.crm.activity.service.ActivityService;
import ekol.event.annotation.ConsumesWebEvent;


@RestController
@RequestMapping("/event-consumer")
public class AccountContactUpdateEventConsumer {

	@Autowired
	private ActivityService activityService;

    @ConsumesWebEvent(event="contact-update", name="activity participant update")
    @PostMapping("/contact-update")
    public void consumeUpdateAccountContact(@RequestBody Contact message) {
    	activityService.updateActivityParticipantsName(message);
    }
}
