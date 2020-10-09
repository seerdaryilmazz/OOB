package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.model.FinancialInfo;
import ekol.agreement.domain.enumaration.StampTaxPayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FinancialInfoJson {
    private BigDecimal contractAmount;
    private String contractAmountCurrency;
    private Integer paymentDueDays;
    private StampTaxPayer stampTaxPayer;
    private BigDecimal stampTaxAmount;
    private String stampTaxCurrency;
    private LocalDate stampTaxDueDate;
    private Boolean paid;

    public FinancialInfo toEntity() {
        return FinancialInfo.builder()
                .contractAmount(getContractAmount())
                .contractAmountCurrency(getContractAmountCurrency())
                .paymentDueDays(getPaymentDueDays())
                .stampTaxPayer(getStampTaxPayer())
                .stampTaxAmount(getStampTaxAmount())
                .stampTaxCurrency(getStampTaxCurrency())
                .stampTaxDueDate(getStampTaxDueDate())
                .paid(getPaid()).build();
    }

    public static FinancialInfoJson fromEntity(FinancialInfo financialInfo) {
        return new FinancialInfoJsonBuilder()
                .contractAmount(financialInfo.getContractAmount())
                .contractAmountCurrency(financialInfo.getContractAmountCurrency())
                .paymentDueDays(financialInfo.getPaymentDueDays())
                .stampTaxPayer(financialInfo.getStampTaxPayer())
                .stampTaxAmount(financialInfo.getStampTaxAmount())
                .stampTaxCurrency(financialInfo.getStampTaxCurrency())
                .stampTaxDueDate(financialInfo.getStampTaxDueDate())
                .paid(financialInfo.getPaid()).build();
    }
}
