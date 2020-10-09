package ekol.authorization.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamUser {
	private Node team;
	private List<UserDto> users = new ArrayList<>();
	
}
