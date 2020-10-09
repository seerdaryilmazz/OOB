package ekol.authorization.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class AuthLevelDto {
	private IdNamePair user;
	private List<Node> nodes;
}
