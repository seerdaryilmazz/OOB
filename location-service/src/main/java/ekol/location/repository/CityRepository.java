package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.City;

/**
 * Created by ozer on 13/12/16.
 */
public interface CityRepository extends ApplicationRepository<City> {

    Iterable<City> findAllByOrderByNameAsc();
}
