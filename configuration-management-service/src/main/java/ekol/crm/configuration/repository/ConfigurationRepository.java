package ekol.crm.configuration.repository;

import java.util.List;

import ekol.crm.configuration.domain.*;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

public interface ConfigurationRepository extends ApplicationMongoRepository<Configuration> {
	List<Configuration> findBySubsidiaryId(Long subsidiaryId);
	List<Configuration> findByKey(ConfigurationKey key);
	List<Configuration> findBySubsidiaryIsNull();
}
