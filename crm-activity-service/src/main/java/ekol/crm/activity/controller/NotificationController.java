package ekol.crm.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.activity.service.NotificationService;
import ekol.model.CodeNamePair;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {
	
	private NotificationService notificationService;
	
	@GetMapping("/notify-for-expired-activities")
	public Iterable<CodeNamePair> notifyForExpiredActivities(@RequestParam String concern){
		return notificationService.notifyForExpiredActivities(concern);
	}
}
