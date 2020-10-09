package ekol.orders.lookup.repository;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.orders.lookup.domain.PackageType;

import java.util.List;

/**
 * Created by kilimci on 18/08/16.
 */
public interface PackageTypeRepository extends LookupRepository<PackageType> {

    public Iterable<PackageType> findByPackageGroupId(Long id);
    List<PackageType> findByOrderByRankAscNameAsc();

}
