package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingItemJson {

    private Long id;

    private String name;

    private String description;

    private String code;

    private String serviceArea;

}
