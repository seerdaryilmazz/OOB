package ekol.outlook.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.annotation.ConsumesWebEvent;
import ekol.outlook.model.OutlookEvent;
import ekol.outlook.model.OutlookEvent.Attendee;
import ekol.outlook.service.OutlookService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerController {

    private OutlookService outlookService;

    @PostMapping("/outlook-event-update")
    @ConsumesWebEvent(event = "outlook-event-update", name = "outlook event updated")
    public void consumeOutlookEventUpdate(@RequestBody OutlookEvent message){
    	message.getAttendees()
    	.stream()
    	.filter(Attendee::isOrganizer)
    	.findFirst().ifPresent(orginizer->outlookService.createOrUpdateEvent(orginizer.getEmail(), message));
    }

}
