package ekol.agreement.queue.repository;

import ekol.agreement.queue.domain.RainbowQueueItem;
import ekol.agreement.queue.enums.Status;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Dogukan Sahinturk on 24.09.2019
 */
public interface RainbowQueueRepository extends ApplicationMongoRepository<RainbowQueueItem> {

    Optional<Collection<RainbowQueueItem>> findByAgreementJson_IdAndStatusIs(Long id, Status status);
}
