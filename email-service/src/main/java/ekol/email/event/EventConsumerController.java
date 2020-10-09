package ekol.email.event;


import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.email.domain.EmailMessage;
import ekol.email.service.*;
import ekol.event.annotation.ConsumesWebEvent;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/email")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerController {

	private EmailService emailService;
	private EmailTransportService emailTransportService;

	@PostMapping("/email-message-created")
	@ConsumesWebEvent(event = "email-message-created", name = "consume email message created event")
	public void consumeEmailMessageCreatedEvent(@RequestBody EmailMessage emailMessage) {
		if(Objects.isNull(emailMessage.getId())){
			emailService.sendMail(emailMessage);
			return;
		}

		emailTransportService.transport(emailMessage);
	}
}
