package ekol.email.repository;

import ekol.email.domain.EmailBlacklist;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

public interface EmailBlacklistRepository extends ApplicationMongoRepository<EmailBlacklist> {
}
