package ekol.notification.domain.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.notification.domain.*;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationJson {
	private int unreadCount;
	private Iterable<Notification> newNotifications;
	private Iterable<Notification> lastNotifications;
	
	private Set<Concern> activeConcerns;
}
