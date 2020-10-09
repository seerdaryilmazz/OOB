package ekol.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {

	private String id;
	private boolean pushed;
	private boolean read;
	private String username;

}
