package ekol.authorization.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class User {

    private Long id;

    private String username;

    private String displayName;

    private String firstLevel;

    private String secondLevel;

    private String thirdLevel;

    private boolean deleted;
}
