package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgreementExtensionJson {
    private Long agreementId;
    private LocalDate oldEndDate;
    private LocalDate newEndDate;
    private String changedBy;
}
