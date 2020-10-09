package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.DistributionRegionTag;

public interface DistributionRegionTagRepository extends ApplicationRepository<DistributionRegionTag> {

    Iterable<DistributionRegionTag> findAllByDistributionRegionId(Long distributionRegionId);

    DistributionRegionTag findByDistributionRegionIdAndValue(Long distributionRegionId, String value);
    
}
