package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.comnon.Location;

/**
 * Created by burak on 05/04/17.
 */
public interface LocationRepository extends ApplicationRepository<Location> {

    public Location findById(Long id);

}
