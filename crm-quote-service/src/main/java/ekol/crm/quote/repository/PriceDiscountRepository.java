package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.Price;
import ekol.crm.quote.domain.model.PriceDiscount;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public interface PriceDiscountRepository extends ApplicationRepository<PriceDiscount> {

    Optional<PriceDiscount> findById(Long id);
    List<PriceDiscount> findByPrice(Price price);
}
