package ekol.crm.inbound.repository;

import ekol.crm.inbound.domain.Inbound;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

public interface InboundRepository extends ApplicationMongoRepository<Inbound> {
	Inbound findByMessageId(String messageId);
}
