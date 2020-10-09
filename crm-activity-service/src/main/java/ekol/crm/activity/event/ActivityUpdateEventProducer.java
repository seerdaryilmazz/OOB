package ekol.crm.activity.event;


import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.crm.activity.domain.Activity;
import ekol.crm.activity.domain.ActivityChange;
import ekol.crm.activity.domain.dto.ActivityJson;
import ekol.crm.activity.event.dto.ActivityEvent;
import ekol.crm.activity.event.dto.OutlookEvent;
import ekol.event.annotation.ProducesEvent;

@Component
public class ActivityUpdateEventProducer {

    @ProducesEvent(event = "outlook-event-update")
    @TransactionalEventListener(fallbackExecution = true, condition = "#event.operation == T(ekol.crm.activity.event.dto.Operation).OUTLOOK_EVENT")
    public OutlookEvent produce(ActivityEvent event) {
        return OutlookEvent.fromActivity(event.getData(Activity.class));
    }

    @ProducesEvent(event = "crm-activity-create")
    @TransactionalEventListener(fallbackExecution = true, condition="#event.operation == T(ekol.crm.activity.event.dto.Operation).CREATE")
    public ActivityJson produceCreateActivity(ActivityEvent event) {
    	return ActivityJson.fromEntity(event.getData(Activity.class));
    }

    @ProducesEvent(event = "crm-activity-update")
    @TransactionalEventListener(fallbackExecution = true, condition="#event.operation == T(ekol.crm.activity.event.dto.Operation).UPDATE")
    public ActivityChange produceUpdateActivity(ActivityEvent event) {
    	return event.getData(ActivityChange.class);
    }

    @ProducesEvent(event = "crm-activity-delete")
    @TransactionalEventListener(fallbackExecution = true, condition="#event.operation == T(ekol.crm.activity.event.dto.Operation).DELETE")
    public ActivityJson produceDeleteActivity(ActivityEvent event) {
    	return ActivityJson.fromEntity(event.getData(Activity.class));
    }
}
