package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.DistributionRegionToPolygonRegion;

public interface DistributionRegionToPolygonRegionRepository extends ApplicationRepository<DistributionRegionToPolygonRegion> {

    Iterable<DistributionRegionToPolygonRegion> findAllByDistributionRegionId(Long distributionRegionId);

    Iterable<DistributionRegionToPolygonRegion> findAllByPolygonRegion_CountryIsoAlpha3Code(String countryIsoAlpha3Code);
    
}
