package ekol.agreement.queue.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class BillingItemJson {
    private Long id;
    private String name;
    private String description;
    private String code;
    private String serviceArea;
}
