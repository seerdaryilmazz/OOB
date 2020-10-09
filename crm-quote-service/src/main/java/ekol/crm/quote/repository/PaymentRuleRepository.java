package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.PaymentRule;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.Optional;

public interface PaymentRuleRepository extends ApplicationRepository<PaymentRule> {

    Optional<PaymentRule> findById(Long id);
}
