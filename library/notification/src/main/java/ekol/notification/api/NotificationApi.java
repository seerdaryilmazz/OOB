package ekol.notification.api;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.notification.dto.*;

@Component
@ComponentScan
public class NotificationApi {
	
	@Value("${oneorder.notification-service}")
	private String notificationServiceName;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public List<Notification> sendNotification(String concern, NotificationDataBuilder builder) {
		return Optional.ofNullable(restTemplate.postForObject(notificationServiceName + "/build?concern={concern}", builder.build(), Notification[].class, concern)).map(Arrays::asList).orElseGet(Collections::emptyList);
	}
	
	public List<Notification> sendNotification(String concern, Object data, String... username) {
		return Optional.ofNullable(restTemplate.postForObject(notificationServiceName + "/build?concern={concern}", NotificationBuild.with(data, username), Notification[].class, concern)).map(Arrays::asList).orElseGet(Collections::emptyList);
	}
	
	public List<Notification> sendNotification(String concern, Object data, Long... userId) {
		return Optional.ofNullable(restTemplate.postForObject(notificationServiceName + "/build?concern={concern}", NotificationBuild.with(data, userId), Notification[].class, concern)).map(Arrays::asList).orElseGet(Collections::emptyList);
	}
	
	public List<Notification> sendNotificationToAuthOperation(String concern, Object data, String... authOperation) {
		return Optional.ofNullable(restTemplate.postForObject(notificationServiceName + "/build?concern={concern}", NotificationBuild.withAuthOperation(data, authOperation), Notification[].class, concern)).map(Arrays::asList).orElseGet(Collections::emptyList);
	}
	
	public Notification getNotification(String id) {
		return restTemplate.getForObject(notificationServiceName + "/notification/{id}", Notification.class, id);
	}
}
