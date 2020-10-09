package ekol.crm.inbound.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	private String username;
	private String displayName;
	private String email;
}
