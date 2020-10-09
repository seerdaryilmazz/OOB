package ekol.notification.client.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUser implements Serializable {
	private Long id;
	private String name;
	private Long externalId;
}
