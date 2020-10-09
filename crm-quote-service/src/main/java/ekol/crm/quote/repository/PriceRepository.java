package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.Price;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends ApplicationRepository<Price> {

    Optional<Price> findById(Long id);
    List<Price> findByQuote(Quote quote);
}
