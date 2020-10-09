package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.enumaration.ApographType;
import ekol.agreement.domain.model.LegalInfo;
import ekol.model.IsoNamePair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LegalInfoJson {
    private String court;
    private IsoNamePair law;
    private Integer numberOfApographs;
    private ApographType apographType;
    private Integer numberOfPapers;
    private LocalDate terminationDate;
    private String terminationReason;

    public LegalInfo toEntity() {
        return LegalInfo.builder()
                .court(getCourt())
                .law(getLaw())
                .numberOfApographs(getNumberOfApographs())
                .apographType(getApographType())
                .numberOfPapers(getNumberOfPapers())
                .terminationDate(getTerminationDate())
                .terminationReason(getTerminationReason()).build();
    }

    public static LegalInfoJson fromEntity(LegalInfo legalInfo) {
        return new LegalInfoJsonBuilder()
                .court(legalInfo.getCourt())
                .law(legalInfo.getLaw())
                .numberOfApographs(legalInfo.getNumberOfApographs())
                .apographType(legalInfo.getApographType())
                .numberOfPapers(legalInfo.getNumberOfPapers())
                .terminationDate(legalInfo.getTerminationDate())
                .terminationReason(legalInfo.getTerminationReason()).build();
    }
}
