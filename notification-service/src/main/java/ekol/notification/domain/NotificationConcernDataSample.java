package ekol.notification.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Document(collection="notification-concern-data-sample")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationConcernDataSample {
	
	@Id
	private String id;
	private Concern concern;
	private Object data;
	
	public static NotificationConcernDataSample with(Concern concern) {
		NotificationConcernDataSample instance = new NotificationConcernDataSample();
		instance.setConcern(concern);
		return instance;
	}
	
}
