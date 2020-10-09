package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.PaymentRule;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PaymentRuleValidator {

    public void validate(PaymentRule paymentRule){
        if(paymentRule.getInvoiceCompany() == null || paymentRule.getInvoiceCompany().getId() == null){
            throw new ValidationException("Invoice company should not be empty");
        }
        if(paymentRule.getType() == null){
            throw new ValidationException("Payment type should not be empty");
        }
        if(paymentRule.getPaymentDueDays() == null){
            throw new ValidationException("Payment due days should not be empty");
        }
    }
}
