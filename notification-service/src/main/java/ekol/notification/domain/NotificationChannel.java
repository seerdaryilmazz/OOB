package ekol.notification.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notification-channel")
public class NotificationChannel extends BaseEntity{

	private Channel channel;
	private Status status;
	
}
