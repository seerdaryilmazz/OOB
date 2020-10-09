package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.LinehaulRouteLeg;
import ekol.location.domain.RouteLegType;
import org.springframework.data.jpa.repository.EntityGraph;

public interface LinehaulRouteLegRepository extends ApplicationRepository<LinehaulRouteLeg> {

    @EntityGraph(value = "LinehaulRouteLeg.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRouteLeg> findAllWithDetailsDistinctBy();

    @EntityGraph(value = "LinehaulRouteLeg.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRouteLeg> findAllWithDetailsDistinctByType(RouteLegType type);

    @EntityGraph(value = "LinehaulRouteLeg.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRouteLeg> findAllWithDetailsDistinctByFromId(Long fromId);

    @EntityGraph(value = "LinehaulRouteLeg.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<LinehaulRouteLeg> findAllWithDetailsDistinctByFromIdAndToId(Long fromId, Long toId);

    @EntityGraph(value = "LinehaulRouteLeg.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    LinehaulRouteLeg findWithDetailsById(Long id);

    LinehaulRouteLeg findByTypeAndFromIdAndToId(RouteLegType type, Long fromId, Long toId);

}
