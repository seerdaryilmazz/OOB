package ekol.crm.activity.event;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.activity.domain.Activity;
import ekol.crm.activity.domain.dto.ActivityJson;
import ekol.crm.activity.event.dto.AccountMerge;
import ekol.crm.activity.service.ActivityService;
import ekol.event.annotation.ConsumesWebEvent;

@RestController
@RequestMapping("/event-consumer")
public class AccountMergeEventConsumer {
	
	@Autowired
	private ActivityService activityService;
	
    @ConsumesWebEvent(event= "account-merge", name= "consume activities in account merge")
    @PostMapping("/account-merge")
    public void consumeMergeAccountEvent(@RequestBody AccountMerge accountMerge) {
    	
    	Set<String> activityIds = accountMerge.getActivities().stream().map(ActivityJson:: getId).collect(Collectors.toSet());
    	
    	activityService.updateAccount(activityIds, accountMerge.getAccount());
    	
    }

}
