package ekol.notification.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection="notification")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification extends BaseEntity{
	
	@DBRef
	private NotificationTemplate template;

	private String subject;
	private String content;
	private Object body;
	private String url;
	
	private boolean pushed;
	private boolean read;
	
	@Indexed
	private String username;
	private String email;

}
