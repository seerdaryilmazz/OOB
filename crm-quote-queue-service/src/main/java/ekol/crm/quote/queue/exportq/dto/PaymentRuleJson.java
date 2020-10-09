package ekol.crm.quote.queue.exportq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRuleJson {

    private IdNamePair invoiceCompany;
    private IdNamePair invoiceLocation;
    private String type;
    private Integer paymentDueDays;
    private String shipOwnerCompanyCode;
    private String airlineCompanyCode;

}
