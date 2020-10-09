package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.DistributionRegion;
import org.springframework.data.jpa.repository.EntityGraph;

public interface DistributionRegionRepository extends ApplicationRepository<DistributionRegion> {

    Iterable<DistributionRegion> findAllByOperationRegionId(Long operationRegionId);

    DistributionRegion findByName(String name);

    @EntityGraph(value = "DistributionRegion.query.one", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<DistributionRegion> findAllAccordingToQueryOneDistinctBy();

    @EntityGraph(value = "DistributionRegion.query.two", type = EntityGraph.EntityGraphType.LOAD)
    DistributionRegion findAccordingToQueryTwoById(Long id);

}
