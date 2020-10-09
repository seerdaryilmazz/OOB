package ekol.crm.account.repository;

import ekol.crm.account.domain.enumaration.CountryStatus;
import ekol.crm.account.domain.model.Country;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends ApplicationRepository<Country> {

    Optional<Country> findById(Long id);
    Optional<Country> findByIsoAndStatus(String iso, CountryStatus status);
    Optional<Country> findByNameIgnoreCaseAndStatus(String name, CountryStatus status);
    List<Country> findByStatusOrderByRankAscNameAsc(CountryStatus status);

}
