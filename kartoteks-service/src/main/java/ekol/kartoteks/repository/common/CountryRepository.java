package ekol.kartoteks.repository.common;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.common.Country;

import java.util.List;

/**
 * Created by fatmaozyildirim on 3/18/16.
 */
public interface CountryRepository extends ApplicationRepository<Country> {

    Country findByIsoIgnoreCase(String iso);
    List<Country> findByWorkingWithOrderByCountryName(boolean workingWith);
    List<Country> findAllByOrderByCountryName();
    List<Country> findByIsoStartingWithIgnoreCase(String iso);
    List<Country> findByEuMember(boolean isMember);


}
