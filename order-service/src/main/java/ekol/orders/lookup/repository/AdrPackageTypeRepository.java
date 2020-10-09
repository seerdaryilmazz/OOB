package ekol.orders.lookup.repository;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.orders.lookup.domain.AdrPackageType;

import java.util.Optional;

public interface AdrPackageTypeRepository extends LookupRepository<AdrPackageType> {

    Optional<AdrPackageType> findById(Long id);
}
