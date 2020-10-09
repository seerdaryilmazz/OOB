package ekol.crm.quote.service;

import ekol.crm.quote.domain.model.PaymentRule;
import ekol.crm.quote.repository.PaymentRuleRepository;
import ekol.crm.quote.validator.PaymentRuleValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentRuleCrudService {

    private PaymentRuleRepository repository;
    private PaymentRuleValidator validator;

    @Transactional
    public PaymentRule save(PaymentRule request){
        this.validator.validate(request);
        return repository.save(request);
    }

    @Transactional
    public void delete(PaymentRule paymentRule){
        paymentRule.setDeleted(true);
        repository.save(paymentRule);
    }
}
