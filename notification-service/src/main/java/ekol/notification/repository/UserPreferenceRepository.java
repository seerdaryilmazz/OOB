package ekol.notification.repository;

import java.util.Set;

import ekol.mongodb.domain.repository.ApplicationMongoRepository;
import ekol.notification.domain.UserPreference;

public interface UserPreferenceRepository extends ApplicationMongoRepository<UserPreference> {
	Set<UserPreference> findByUsername(String username);
}
