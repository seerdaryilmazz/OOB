package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.model.HistoryModel;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.*;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HistoryModelJson {

    private Long id;
    private String name;
    private Integer eur;
    private Integer usd;
    private Integer inflation;
    private Integer minimumWage;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private String notes;
    private Long modelId;
    private UtcDateTime lastUpdated;

    public HistoryModel toEntity(){
        return HistoryModel.builder()
                .id(getId())
                .name(getName())
                .eur(getEur())
                .usd(getUsd())
                .inflation(getInflation())
                .minimumWage(getMinimumWage())
                .validityStartDate(getValidityStartDate())
                .validityEndDate(getValidityEndDate())
                .notes(getNotes())
                .modelId(getModelId()).build();
    }

    public static HistoryModelJson fromEntity(HistoryModel historyModel){
        return new HistoryModelJsonBuilder()
                .id(historyModel.getId())
                .name(historyModel.getName())
                .eur(historyModel.getEur())
                .usd(historyModel.getUsd())
                .inflation(historyModel.getInflation())
                .minimumWage(historyModel.getMinimumWage())
                .validityStartDate(historyModel.getValidityStartDate())
                .validityEndDate(historyModel.getValidityEndDate())
                .notes(historyModel.getNotes())
                .modelId(historyModel.getModelId()).build();
    }

}
