package ekol.outlook.repository;

import ekol.mongodb.domain.repository.ApplicationMongoRepository;
import ekol.outlook.model.OutlookEvent;


public interface EventRepository extends ApplicationMongoRepository<OutlookEvent> {
    OutlookEvent findBySourceId(Long sourceId);
}
