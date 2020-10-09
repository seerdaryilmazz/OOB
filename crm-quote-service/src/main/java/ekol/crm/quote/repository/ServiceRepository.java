package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.Service;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface ServiceRepository extends ApplicationRepository<Service> {

    List<Service> findByQuote(Quote quote);
}
