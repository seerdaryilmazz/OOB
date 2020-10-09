package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.CollectionRegionToPolygonRegion;

public interface CollectionRegionToPolygonRegionRepository extends ApplicationRepository<CollectionRegionToPolygonRegion> {

    Iterable<CollectionRegionToPolygonRegion> findAllByCollectionRegionId(Long collectionRegionId);

    Iterable<CollectionRegionToPolygonRegion> findAllByPolygonRegion_CountryIsoAlpha3Code(String countryIsoAlpha3Code);
    
}
