package ekol.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.notification.domain.*;
import ekol.notification.service.NotificationTemplateService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/template")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationTemplateController {
	
	private NotificationTemplateService notificationTemplateService; 

	@Authorize(operations = "notification.manage")
	@PostMapping({"/", ""})
    public NotificationTemplate add(@RequestBody NotificationTemplate entity) {
        return notificationTemplateService.add(entity);
    }

	@Authorize(operations = "notification.manage")
    @PutMapping({"/{id}/", "/{id}"})
    public NotificationTemplate update(@PathVariable String id, @RequestBody NotificationTemplate entity) {
    	return notificationTemplateService.update(id, entity);
    }

	@Authorize(operations = "notification.manage")
    @GetMapping({"/", ""})
    public Iterable<NotificationTemplate> findAll() {
        return notificationTemplateService.findAll();
    }

	@Authorize(operations = "notification.manage")
    @GetMapping({"/{id}/", "/{id}"})
    public NotificationTemplate get(@PathVariable String id) {
        return notificationTemplateService.get(id);
    }

	@Authorize(operations = "notification.manage")
    @DeleteMapping({"/{id}/", "/{id}"})
    public void delete(@PathVariable String id) {
    	notificationTemplateService.delete(id);
    }
	
	@Authorize(operations = "notification.manage")
	@GetMapping("/by-channel")
	public List<NotificationTemplate> listByChannel(@RequestParam Channel channel) {
		return notificationTemplateService.findTemplate(channel, null);
	}
	
	@Authorize(operations = "notification.manage")
	@PatchMapping("/{id}")
	public NotificationTemplate patch(@PathVariable String id, NotificationTemplate request) {
		return notificationTemplateService.patch(id, request);
	}
}
