package ekol.kartoteks.domain.dto;

import ekol.kartoteks.domain.CompanyImportQueue;
import ekol.model.User;

/**
 * Created by kilimci on 01/03/2017.
 */
public class QueueItemCompleteEvent {

    private Long queueItemId;
    private User user;

    public static QueueItemCompleteEvent with(CompanyImportQueue queueItem, User user){
        QueueItemCompleteEvent event = new QueueItemCompleteEvent();
        event.setQueueItemId(queueItem.getId());
        event.setUser(user);
        return event;
    }

    public Long getQueueItemId() {
        return queueItemId;
    }

    public void setQueueItemId(Long queueItemId) {
        this.queueItemId = queueItemId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
