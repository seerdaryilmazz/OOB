package ekol.agreement.domain.model;


import ekol.agreement.domain.dto.agreement.AgreementExtensionJson;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AgreementExtension {
    private Long agreementId;
    private LocalDate oldEndDate;
    private LocalDate newEndDate;
    private String changedBy;

    public AgreementExtensionJson toJson(){
        AgreementExtensionJson json = new AgreementExtensionJson();
        json.setAgreementId(agreementId);
        json.setOldEndDate(oldEndDate);
        json.setNewEndDate(newEndDate);
        json.setChangedBy(changedBy);
        return json;
    }
}
