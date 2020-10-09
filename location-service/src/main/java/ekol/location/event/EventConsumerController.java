package ekol.location.event;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.annotation.ConsumesWebEvent;
import ekol.location.domain.dto.TripPlanEventMessage;
import ekol.location.domain.location.customs.CustomsOffice;
import ekol.location.service.*;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 24/11/2017.
 */
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/event-consumer")
public class EventConsumerController {

    private RouteLegExpeditionTripService routeLegExpeditionTripService;
    private LocationUpdatedEventConsumeService locationUpdatedEventConsumeService;

    @PostMapping("/trip-plan-created")
    @ConsumesWebEvent(event = "trip-plan-created", name = "update expedition usage for create trip plan")
    public void consumeTripPlanCreater(@RequestBody TripPlanEventMessage message){
        routeLegExpeditionTripService.updateExpeditions(message);
    }
    
    @PostMapping("/company-updated")
    @ConsumesWebEvent(event = "company-updated", name = "update location names")
    public void consumeCompanyUpdate(@RequestBody Map<String,Object> message) {
    	Optional.ofNullable(message.get("id"))
    		.map(String::valueOf)
    		.map(BigInteger::new)
    		.map(BigInteger::longValue)
    		.ifPresent(locationUpdatedEventConsumeService::consumeCompany);
    }
    
    @PostMapping("/customs-office-updated")
    @ConsumesWebEvent(event = "customs-office-updated", name = "update location names of customs office")
    public void consumeCustomsOfficeUpdate(@RequestBody CustomsOffice message) {
    	Optional.ofNullable(message).ifPresent(locationUpdatedEventConsumeService::consumeCustomsOffice);
    }
}
