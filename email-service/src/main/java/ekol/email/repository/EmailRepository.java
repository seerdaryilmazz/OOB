package ekol.email.repository;

import org.springframework.data.domain.*;

import ekol.email.domain.Email;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

public interface EmailRepository extends ApplicationMongoRepository<Email> {
	Page<Email> findBySource(String source, Pageable page);
}
