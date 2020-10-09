package ekol.crm.history.event.dto.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class PaymentRuleJson {

    private Long id;
    private IdNamePair invoiceCompany;
    private IdNamePair invoiceLocation;
    private CodeNamePair type;
    private Integer paymentDueDays;

}
