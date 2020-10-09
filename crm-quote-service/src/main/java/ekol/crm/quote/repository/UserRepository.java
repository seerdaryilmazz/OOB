package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.User;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.Optional;

public interface UserRepository extends ApplicationRepository<User> {

    Optional<User> findById(Long id);
}
