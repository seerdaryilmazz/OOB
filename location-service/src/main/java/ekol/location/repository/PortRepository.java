package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.port.Port;

import java.util.List;

/**
 * Created by ozer on 01/11/16.
 */
public interface PortRepository extends ApplicationRepository<Port> {

    public Port findById(Long id);

    Iterable<Port> findAllByOrderByNameAsc();

    public List<Port> findByRouteLegStopId(Long routeLegStopId);


}
