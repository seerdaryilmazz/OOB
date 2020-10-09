package ekol.agreement.queue.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeJson {
    private String changedBy;
    private String changeTime;
    private String description;
}
