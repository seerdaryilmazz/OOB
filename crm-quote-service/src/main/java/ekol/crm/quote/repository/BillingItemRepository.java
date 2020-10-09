package ekol.crm.quote.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.*;

import ekol.crm.quote.domain.model.BillingItem;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface BillingItemRepository extends ApplicationRepository<BillingItem> {
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable("billing-item-cache")
    Iterable<BillingItem> findAll();
}
