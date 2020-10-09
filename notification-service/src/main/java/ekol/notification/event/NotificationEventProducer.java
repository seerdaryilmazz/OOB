package ekol.notification.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.event.annotation.ProducesEvent;
import ekol.notification.domain.Notification;
import ekol.notification.domain.dto.*;
import ekol.notification.event.dto.*;

@Component
public class NotificationEventProducer {
	
	@ProducesEvent(event = "notification-build")
	@TransactionalEventListener(fallbackExecution = true)
	public NotificationBuild notificationBuild(NotificationBuildEvent event) {
		return event.getData();
	}
	
	@ProducesEvent(event = "notification-created")
	@TransactionalEventListener(fallbackExecution = true)
	public Notification notificationCreated(NotificationEvent event) {
		return event.getData();
	}
	
	@ProducesEvent(event = "email-message-created")
    @TransactionalEventListener(fallbackExecution = true)
    public EmailMessage produceEmailMessageCreated(EmailEvent event) {
            return new EmailMessage(
            		event.getSource(),
            		event.getConcern(),
            		event.getTo(),
                    event.getSubject(),
                    event.getBody(),
                    event.getHtml());
    }
}
