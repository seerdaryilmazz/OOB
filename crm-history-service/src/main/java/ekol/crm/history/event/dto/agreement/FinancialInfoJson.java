package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class FinancialInfoJson {
    private BigDecimal contractAmount;
    private String contractAmountCurrency;
    private Integer paymentDueDays;
    private CodeNamePair stampTaxPayer;
    private BigDecimal stampTaxAmount;
    private String stampTaxCurrency;
    private LocalDate stampTaxDueDate;
    private Boolean paid;
}
