package ekol.notification.domain.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.notification.client.dto.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationBuildSource {
	
	private Set<String> authOperations;
	private Set<User> targetUsers;
	private Object data;

}