package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.model.OwnerCompany;
import ekol.crm.quote.domain.model.PaymentRule;
import ekol.crm.quote.domain.enumaration.PaymentType;
import ekol.exceptions.ValidationException;
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
    private PaymentType type;
    private Integer paymentDueDays;
    private OwnerCompany ownerCompany;

    public PaymentRule toEntity(){
        return PaymentRule.builder()
                .id(getId())
                .invoiceCompany(getInvoiceCompany())
                .invoiceLocation(getInvoiceLocation())
                .type(getType())
                .paymentDueDays(getPaymentDueDays())
                .ownerCompany(getOwnerCompany()).build();
    }
    public static PaymentRuleJson fromEntity(PaymentRule paymentRule){
        return new PaymentRuleJsonBuilder()
                .id(paymentRule.getId())
                .invoiceCompany(paymentRule.getInvoiceCompany())
                .invoiceLocation(paymentRule.getInvoiceLocation())
                .type(paymentRule.getType())
                .paymentDueDays(paymentRule.getPaymentDueDays())
                .ownerCompany(paymentRule.getOwnerCompany()).build();
    }

    public void validate(){
        if(getInvoiceCompany() == null || getInvoiceCompany().getId() == null){
            throw new ValidationException("Invoice company should not be empty");
        }
        if(getInvoiceLocation() == null || getInvoiceLocation().getId() == null){
            throw new ValidationException("Invoice location should not be empty");
        }
        if(getType() == null){
            throw new ValidationException("Payment type should not be empty");
        }
        if(getPaymentDueDays() == null){
            throw new ValidationException("Payment due days should not be empty");
        }
    }
}
