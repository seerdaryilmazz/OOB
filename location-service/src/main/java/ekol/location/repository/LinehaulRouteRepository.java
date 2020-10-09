package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.LinehaulRoute;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface LinehaulRouteRepository extends ApplicationRepository<LinehaulRoute> {

    @EntityGraph(value = "LinehaulRoute.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRoute> findAllWithDetailsDistinctByOrderByNameAsc();

    @EntityGraph(value = "LinehaulRoute.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRoute> findAllWithDetailsDistinctByFromIdOrderByNameAsc(Long fromId);

    @EntityGraph(value = "LinehaulRoute.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRoute> findAllWithDetailsDistinctByFromIdAndToIdOrderByNameAsc(Long fromId, Long toId);

    @EntityGraph(value = "LinehaulRoute.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    LinehaulRoute findWithDetailsById(Long id);

    @EntityGraph(value = "LinehaulRoute.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRoute> findWithDetailsByIdIn(List<Long> idList);

    LinehaulRoute findByName(String name);

}
