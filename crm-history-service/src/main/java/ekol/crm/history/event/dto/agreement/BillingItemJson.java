package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class BillingItemJson {
    private Long id;
    private String name;
    private String description;
    private String code;
    private String serviceArea;
}
