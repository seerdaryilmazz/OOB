package ekol.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.notification.domain.*;
import ekol.notification.domain.dto.NotificationJson;
import ekol.notification.service.NotificationService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {

	private NotificationService notificationService;
	
	@GetMapping("/my")
	public NotificationJson my(){
		return notificationService.my(Channel.WEB_PUSH);
	}
	
	@GetMapping
	public Iterable<Notification> list(@RequestParam(defaultValue = "100", required = false) Integer limit){
		return notificationService.list(limit, Channel.WEB_PUSH).getContent();
	}
	
	@GetMapping("/{id}")
	public Notification list(@PathVariable String id){
		return notificationService.get(id);
	}
	
	@PutMapping("/read/{id}")
	public NotificationJson read(@PathVariable String id) {
		notificationService.read(id, Boolean.TRUE);
		return my();
	}
	
	@PutMapping("/unread/{id}")
	public NotificationJson unread(@PathVariable String id) {
		notificationService.read(id, Boolean.FALSE);
		return my();
	}
	
	@PutMapping("/read-all")
	public NotificationJson readAll() {
		notificationService.readAll(Boolean.TRUE, Channel.WEB_PUSH);
		return my();
	}
	
	@GetMapping("/post-emails")
	public Iterable<Notification> postEmails() {
		return notificationService.postEmails();
	}
	
}
