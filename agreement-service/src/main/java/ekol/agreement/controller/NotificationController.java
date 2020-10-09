package ekol.agreement.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.agreement.domain.dto.NotificationParametersJson;
import ekol.agreement.service.NotificationService;
import ekol.model.CodeNamePair;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {
	
	private NotificationService notificationService;
	
	@GetMapping("/notify")
	public Collection<CodeNamePair>notify(NotificationParametersJson parameters){
		return notificationService.notify(parameters);
	}

	@GetMapping("/job-names")
	public Collection<String>jobNames(){
		return notificationService.jobNames();
	}
}
