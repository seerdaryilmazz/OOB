package ekol.orders.transportOrder.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.transportOrder.domain.PackageTypeRestriction;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface PackageTypeRestrictionRepository extends ApplicationRepository<PackageTypeRestriction> {

    @EntityGraph(value = "PackageTypeRestriction.withPackageType", type = EntityGraph.EntityGraphType.LOAD)
    PackageTypeRestriction findByPackageTypeIdAndDeletedIsFalse(Long packageTypeId);

    @EntityGraph(value = "PackageTypeRestriction.withPackageType", type = EntityGraph.EntityGraphType.LOAD)
    List<PackageTypeRestriction> findAllByDeletedIsFalse();

    Optional<PackageTypeRestriction> findByPackageTypeId(Long packageTypeId);

}
