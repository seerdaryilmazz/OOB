package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.Load;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface LoadRepository extends ApplicationRepository<Load> {

    List<Load> findByQuote(Quote quote);
}
