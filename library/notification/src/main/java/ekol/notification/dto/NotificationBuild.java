package ekol.notification.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationBuild implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object data;
	private Set<User> targetUsers;
	private Set<String> authOperations;
	
	public static NotificationBuild with(Object data, String... targetUsers) {
		NotificationBuild instance = new NotificationBuild();
		instance.setData(data);
		instance.setTargetUsers(Stream.of(targetUsers).parallel().distinct().map(User::with).collect(Collectors.toSet()));
		return instance;
	}
	
	public static NotificationBuild with(Object data, Long... targetUsers) {
		NotificationBuild instance = new NotificationBuild();
		instance.setData(data);
		instance.setTargetUsers(Stream.of(targetUsers).parallel().distinct().map(User::with).collect(Collectors.toSet()));
		return instance;
	}
	
	public static NotificationBuild withAuthOperation(Object data, String... authOperations) {
		NotificationBuild instance = new NotificationBuild();
		instance.setData(data);
		instance.setAuthOperations(Stream.of(authOperations).collect(Collectors.toSet()));
		return instance;
	}

}
