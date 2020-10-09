package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.enumaration.EkolOrCustomer;
import ekol.agreement.domain.model.SignatureInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignatureInfoJson {
    private Long id;
    @NotNull(message = "Signature Info signed by can not be null")
    private EkolOrCustomer signedBy;
    @NotNull(message = "Signature Info name can not be null")
    private String name;
    private String title;
    @NotNull(message = "Signature Info date can not be null")
    private LocalDate signedDate;
    private String place;

    public SignatureInfo toEntity() {
        return SignatureInfo.builder()
                .id(getId())
                .signedBy(getSignedBy())
                .name(getName())
                .title(getTitle())
                .signedDate(getSignedDate())
                .place(getPlace()).build();
    }

    public static SignatureInfoJson fromEntity(SignatureInfo signatureInfo) {
        return new SignatureInfoJsonBuilder()
                .id(signatureInfo.getId())
                .signedBy(signatureInfo.getSignedBy())
                .name(signatureInfo.getName())
                .title(signatureInfo.getTitle())
                .signedDate(signatureInfo.getSignedDate())
                .place(signatureInfo.getPlace()).build();
    }
}
