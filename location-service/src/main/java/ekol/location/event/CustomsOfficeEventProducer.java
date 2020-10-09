package ekol.location.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.event.annotation.ProducesEvent;
import ekol.location.domain.location.customs.CustomsOffice;

@Component
public class CustomsOfficeEventProducer {

	@ProducesEvent(event="customs-office-created")
	@TransactionalEventListener(fallbackExecution=true, condition="#event.operation == T(ekol.location.event.Operation).CREATE")
	public CustomsOffice customsOfficeCreated(EventMessageWrapper event) {
		return event.getData(CustomsOffice.class);
	}
	
	@ProducesEvent(event="customs-office-updated")
	@TransactionalEventListener(fallbackExecution=true, condition="#event.operation == T(ekol.location.event.Operation).UPDATE")
	public CustomsOffice customsOfficeUpdated(EventMessageWrapper event) {
		return event.getData(CustomsOffice.class);
	}
	
	@ProducesEvent(event="customs-office-deleted")
	@TransactionalEventListener(fallbackExecution=true, condition="#event.operation == T(ekol.location.event.Operation).DELETE")
	public CustomsOffice customsOfficeDeleted(EventMessageWrapper event) {
		return event.getData(CustomsOffice.class);
	}
}
