package ekol.notification.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.annotation.ConsumesWebEvent;
import ekol.notification.domain.*;
import ekol.notification.domain.dto.*;
import ekol.notification.service.NotificationBuildService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/build")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationBuildController {
	
	private NotificationBuildService notificationBuildService;
	
	@PostMapping
	public List<Notification> build(@RequestParam Concern concern, @RequestBody NotificationBuildSource source) {
		notificationBuildService.buildAsync(NotificationBuild.with(concern, source));
		return Collections.emptyList();
	}
	
	@PostMapping(value = "/sync")
	@ConsumesWebEvent(event = "notification-build", name = "notification-build")
	public void consumeNotificationBuild(@RequestBody NotificationBuild eventMessage) {
		notificationBuildService.build(eventMessage);
	}
}
