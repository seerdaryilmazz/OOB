package ekol.notification.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
@RequiredArgsConstructor(staticName = "with")
public class NotificationContext {
	@NonNull
	Object[] args;
	Object result;
}
