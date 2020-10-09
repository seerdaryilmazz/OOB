package ekol.notification.event.dto;

import ekol.notification.domain.dto.NotificationBuild;
import lombok.*;

@Data
@AllArgsConstructor(staticName = "with")
public class NotificationBuildEvent {
	private NotificationBuild data;
}
