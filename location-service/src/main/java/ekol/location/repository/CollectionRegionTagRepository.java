package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.CollectionRegionTag;

public interface CollectionRegionTagRepository extends ApplicationRepository<CollectionRegionTag> {

    Iterable<CollectionRegionTag> findAllByCollectionRegionId(Long collectionRegionId);

    CollectionRegionTag findByCollectionRegionIdAndValue(Long collectionRegionId, String value);

}
