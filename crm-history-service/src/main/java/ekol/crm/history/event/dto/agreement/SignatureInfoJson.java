package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class SignatureInfoJson {
    private Long id;
    private CodeNamePair signedBy;
    private String name;
    private String title;
    private LocalDate signedDate;
    private String place;
}
