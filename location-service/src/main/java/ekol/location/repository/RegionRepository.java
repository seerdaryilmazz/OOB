package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.Region;

/**
 * Created by ozer on 13/12/16.
 */
public interface RegionRepository extends ApplicationRepository<Region> {

    Iterable<Region> findAllByOrderByNameAsc();
}

