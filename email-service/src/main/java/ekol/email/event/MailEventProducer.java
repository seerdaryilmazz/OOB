package ekol.email.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.email.domain.EmailMessage;
import ekol.event.annotation.ProducesEvent;

@Component
public class MailEventProducer {

	@ProducesEvent(event = "email-message-created")
    @TransactionalEventListener(fallbackExecution = true)
    public EmailMessage produceEmailMessage(EmailEvent event) {
        return event.getData(EmailMessage.class);
    }
	    
}
