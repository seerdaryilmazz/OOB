package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRuleJson {

    private Long id;
    private IdNamePair invoiceCompany;
    private IdNamePair invoiceLocation;
    private CodeNamePair type;
    private Integer paymentDueDays;
    private OwnerCompanyJson ownerCompany;

}
