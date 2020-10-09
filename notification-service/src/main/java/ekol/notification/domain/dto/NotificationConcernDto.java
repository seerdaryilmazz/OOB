package ekol.notification.domain.dto;

import java.util.*;

import ekol.notification.domain.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class NotificationConcernDto {
	private Concern concern;
	private Status status;
	private List<NotificationTemplate> templates;
}
