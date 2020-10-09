package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.CollectionRegion;
import org.springframework.data.jpa.repository.EntityGraph;

public interface CollectionRegionRepository extends ApplicationRepository<CollectionRegion> {

    Iterable<CollectionRegion> findAllByOperationRegionId(Long operationRegionId);

    CollectionRegion findByName(String name);

    @EntityGraph(value = "CollectionRegion.query.one", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<CollectionRegion> findAllAccordingToQueryOneDistinctBy();

    @EntityGraph(value = "CollectionRegion.query.two", type = EntityGraph.EntityGraphType.LOAD)
    CollectionRegion findAccordingToQueryTwoById(Long id);

}
