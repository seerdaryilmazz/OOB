package ekol.notification.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	
	public static User with(Long id) {
		User instance = new User();
		instance.setId(id);
		return instance;
	}
	public static User with(String username) {
		User instance = new User();
		instance.setUsername(username);
		return instance;
	}
}
