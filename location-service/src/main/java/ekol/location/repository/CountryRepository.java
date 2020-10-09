package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.Country;

/**
 * Created by ozer on 13/12/16.
 */
public interface CountryRepository extends ApplicationRepository<Country> {

    Iterable<Country> findAllByOrderByNameAsc();

    Country findByIsoAlpha3Code(String isoAlpha3Code);

    Country findByIso(String iso);

    Country findByName(String name);

}
