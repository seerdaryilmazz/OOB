package ekol.orders.lookup.repository;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.orders.lookup.domain.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodRepository extends LookupRepository<PaymentMethod> {

    Optional<PaymentMethod> findById(Long id);
}
