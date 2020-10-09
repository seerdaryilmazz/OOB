package ekol.notification.domain.dto;

import ekol.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "with")
public class NotificationEvent {
	private Notification data;
}
