package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.model.LetterOfGuarentee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LetterOfGuarenteeJson {
    private Long id;
    private String scope;
    private BigDecimal contractAmount;
    private String currency;
    @NotNull(message = "Letter of Guarantee validity start date can not be null")
    private LocalDate validityStartDate;
    @NotNull(message = "Letter of Guarantee validity end date can not be null")
    private LocalDate validityEndDate;

    public LetterOfGuarentee toEntity() {
        return LetterOfGuarentee.builder()
                .id(getId())
                .scope(getScope())
                .contractAmount(getContractAmount())
                .currency(getCurrency())
                .validityStartDate(getValidityStartDate())
                .validityEndDate(getValidityEndDate()).build();
    }

    public static LetterOfGuarenteeJson fromEntity(LetterOfGuarentee letterOfGuarentee) {
        return new LetterOfGuarenteeJsonBuilder()
                .id(letterOfGuarentee.getId())
                .scope(letterOfGuarentee.getScope())
                .contractAmount(letterOfGuarentee.getContractAmount())
                .currency(letterOfGuarentee.getCurrency())
                .validityStartDate(letterOfGuarentee.getValidityStartDate())
                .validityEndDate(letterOfGuarentee.getValidityEndDate()).build();
    }
}
