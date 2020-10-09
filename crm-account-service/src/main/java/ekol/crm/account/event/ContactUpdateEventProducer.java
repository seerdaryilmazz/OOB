package ekol.crm.account.event;


import ekol.crm.account.domain.model.Contact;
import ekol.crm.account.domain.dto.ContactJson;
import ekol.event.annotation.ProducesEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ContactUpdateEventProducer {

    @ProducesEvent(event = "contact-update")
    @TransactionalEventListener(fallbackExecution = true)
    public ContactJson produceUpdate(Contact contact) {
        return ContactJson.fromEntity(contact);
    }

    @ProducesEvent(event = "contact-delete")
    @TransactionalEventListener(fallbackExecution = true)
    public ContactJson produceDelete(Contact contact) {
        return ContactJson.fromEntity(contact);
    }
}
