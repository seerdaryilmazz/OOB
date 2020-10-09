package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IsoNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class LegalInfoJson {
    private String court;
    private IsoNamePair law;
    private Integer numberOfApographs;
    private CodeNamePair apographType;
    private Integer numberOfPapers;
    private LocalDate terminationDate;
    private String terminationReason;
}
