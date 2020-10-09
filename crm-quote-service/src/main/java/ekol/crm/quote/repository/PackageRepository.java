package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.Package;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends ApplicationRepository<Package> {

    Optional<Package> findById(Long id);
    List<Package> findByQuote(Quote quote);
}
