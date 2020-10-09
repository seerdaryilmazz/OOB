package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.model.PriceAdaptationModel;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriceAdaptationModelJson {
    private Long id;
    @NotNull(message = "Price Adaptation Model name can not be null")
    private String name;
    private Integer eur;
    private Integer usd;
    private Integer inflation;
    private Integer minimumWage;
    @NotNull(message = "Price Adaptation Model validity start date can not be null")
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private String notes;
    private UtcDateTime lastUpdated;


    public PriceAdaptationModel toEntity(){
        return PriceAdaptationModel.builder()
                .id(getId())
                .name(getName())
                .eur(getEur())
                .usd(getUsd())
                .inflation(getInflation())
                .minimumWage(getMinimumWage())
                .validityStartDate(getValidityStartDate())
                .validityEndDate(getValidityEndDate())
                .notes(getNotes()).build();
    }

    public static PriceAdaptationModelJson fromEntity(PriceAdaptationModel priceAdaptationModel){
        if(Objects.isNull(priceAdaptationModel)){
            return null;
        }
        return new PriceAdaptationModelJsonBuilder()
                .id(priceAdaptationModel.getId())
                .name(priceAdaptationModel.getName())
                .eur(priceAdaptationModel.getEur())
                .usd(priceAdaptationModel.getUsd())
                .inflation(priceAdaptationModel.getInflation())
                .minimumWage(priceAdaptationModel.getMinimumWage())
                .validityStartDate(priceAdaptationModel.getValidityStartDate())
                .validityEndDate(priceAdaptationModel.getValidityEndDate())
                .notes(priceAdaptationModel.getNotes())
                .lastUpdated(priceAdaptationModel.getLastUpdated()).build();
    }
}
