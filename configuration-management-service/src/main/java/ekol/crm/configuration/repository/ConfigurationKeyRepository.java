package ekol.crm.configuration.repository;

import ekol.crm.configuration.domain.ConfigurationKey;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

public interface ConfigurationKeyRepository extends ApplicationMongoRepository<ConfigurationKey> {
	ConfigurationKey findByCode(String code);
}
