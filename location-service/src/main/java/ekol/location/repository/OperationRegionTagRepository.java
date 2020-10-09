package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.OperationRegionTag;

public interface OperationRegionTagRepository extends ApplicationRepository<OperationRegionTag> {

    Iterable<OperationRegionTag> findAllByOperationRegionId(Long operationRegionId);

    OperationRegionTag findByOperationRegionIdAndValue(Long operationRegionId, String value);

}
