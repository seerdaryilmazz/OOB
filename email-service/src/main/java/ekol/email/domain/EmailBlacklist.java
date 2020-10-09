package ekol.email.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
@Document(collection = "blacklist")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailBlacklist {

	@Id
	private String id;

	@Indexed
	private String email;
}
