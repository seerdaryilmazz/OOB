package ekol.crm.history.repository;

import ekol.crm.history.domain.History;
import ekol.crm.history.domain.Item;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

import java.util.Optional;


public interface HistoryRepository extends ApplicationMongoRepository<History> {
    Optional<History> findByItem(Item item);
}
