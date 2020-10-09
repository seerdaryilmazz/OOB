package ekol.crm.history.domain.dto.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.history.event.dto.agreement.AgreementExtensionJson;
import lombok.*;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgreementExtension {
    private Long agreementId;
    private LocalDate oldEndDate;
    private LocalDate newEndDate;
    private String changedBy;


    public static AgreementExtension fromJson(AgreementExtensionJson json){
        return new AgreementExtensionBuilder()
                .agreementId(json.getAgreementId())
                .oldEndDate(json.getOldEndDate())
                .newEndDate(json.getNewEndDate())
                .changedBy(json.getChangedBy()).build();
    }
}
