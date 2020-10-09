package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.LinehaulRouteLegStop;
import org.springframework.data.jpa.repository.EntityGraph;

public interface LinehaulRouteLegStopRepository extends ApplicationRepository<LinehaulRouteLegStop> {

    @EntityGraph(value = "LinehaulRouteLegStop.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRouteLegStop> findAllWithDetailsDistinctByOrderByName();

    @EntityGraph(value = "LinehaulRouteLegStop.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    LinehaulRouteLegStop findWithDetailsById(Long id);

    LinehaulRouteLegStop findByName(String name);

}
