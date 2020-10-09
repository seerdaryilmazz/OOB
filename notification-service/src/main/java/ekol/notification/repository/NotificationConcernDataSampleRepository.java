package ekol.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ekol.notification.domain.*;

public interface NotificationConcernDataSampleRepository extends MongoRepository<NotificationConcernDataSample, String> {
	NotificationConcernDataSample findByConcern(Concern concern);
}
