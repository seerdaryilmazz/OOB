package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.OperationRegionToPolygonRegion;

public interface OperationRegionToPolygonRegionRepository extends ApplicationRepository<OperationRegionToPolygonRegion> {

    Iterable<OperationRegionToPolygonRegion> findAllByOperationRegionId(Long operationRegionId);

    Iterable<OperationRegionToPolygonRegion> findAllByPolygonRegion_CountryIsoAlpha3Code(String countryIsoAlpha3Code);

    OperationRegionToPolygonRegion findByPolygonRegionId(Long polygonRegionId);

    OperationRegionToPolygonRegion findByPolygonRegionParentAndPolygonRegionName(String polygonRegionParent, String polygonRegionName);

}
