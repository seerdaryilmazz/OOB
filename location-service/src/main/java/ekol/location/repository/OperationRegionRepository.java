package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.OperationRegion;
import org.springframework.data.jpa.repository.EntityGraph;

public interface OperationRegionRepository extends ApplicationRepository<OperationRegion> {

    OperationRegion findByName(String name);

    @EntityGraph(value = "OperationRegion.query.one", type = EntityGraph.EntityGraphType.LOAD)
    OperationRegion findAccordingToQueryOneById(Long id);

    @EntityGraph(value = "OperationRegion.query.two", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<OperationRegion> findAllAccordingToQueryTwoDistinctByOrderByName();

    @EntityGraph(value = "OperationRegion.query.three", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<OperationRegion> findAllAccordingToQueryThreeDistinctByOrderByName();

    @EntityGraph(value = "OperationRegion.query.four", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<OperationRegion> findAllAccordingToQueryFourDistinctByOrderByName();

    @EntityGraph(value = "OperationRegion.query.three", type = EntityGraph.EntityGraphType.LOAD)
    OperationRegion findAccordingToQueryThreeById(Long id);

    @EntityGraph(value = "OperationRegion.query.five", type = EntityGraph.EntityGraphType.LOAD)
    OperationRegion findAccordingToQueryFiveById(Long id);

    Iterable<OperationRegion> findAllByIdNot(Long id);

}
