package ekol.kartoteks.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class UserJson {
    
    private String displayName;
    private String sapNumber;
}
