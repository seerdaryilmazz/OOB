package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.enumaration.InsuranceType;
import ekol.agreement.domain.enumaration.EkolOrCustomer;
import ekol.agreement.domain.model.InsuranceInfo;
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
public class InsuranceInfoJson {

    private Long id;
    @NotNull(message = "Insurance info type can not be null")
    private InsuranceType insuranceType;
    private BigDecimal exemptionLimit;
    @NotNull(message = "Insurance info insured by can not be null")
    private EkolOrCustomer insuredBy;
    private String currency;
    @NotNull(message = "Insurance info validity start date can not be null")
    private LocalDate validityStartDate;
    @NotNull(message = "Insurance info validity end date can not be null")
    private LocalDate validityEndDate;
    private String coverage;

    public InsuranceInfo toEntity(){
        return InsuranceInfo.builder()
                .id(getId())
                .insuranceType(getInsuranceType())
                .exemptionLimit(getExemptionLimit())
                .insuredBy(getInsuredBy())
                .currency(getCurrency())
                .validityStartDate(getValidityStartDate())
                .validityEndDate(getValidityEndDate())
                .coverage(getCoverage()).build();
    }

    public static InsuranceInfoJson fromEntity(InsuranceInfo insuranceInfo){
        return new InsuranceInfoJsonBuilder()
                .id(insuranceInfo.getId())
                .insuranceType(insuranceInfo.getInsuranceType())
                .exemptionLimit(insuranceInfo.getExemptionLimit())
                .insuredBy(insuranceInfo.getInsuredBy())
                .currency(insuranceInfo.getCurrency())
                .validityStartDate(insuranceInfo.getValidityStartDate())
                .validityEndDate(insuranceInfo.getValidityEndDate())
                .coverage(insuranceInfo.getCoverage()).build();
    }
}
