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
public class InsuranceInfoJson {
    private Long id;
    private CodeNamePair insuranceType;
    private BigDecimal exemptionLimit;
    private CodeNamePair insuredBy;
    private String currency;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private String coverage;
}
