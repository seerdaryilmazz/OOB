package ekol.outlook.repository;

import ekol.mongodb.domain.repository.ApplicationMongoRepository;
import ekol.outlook.model.Account;

import java.util.Optional;


public interface AccountRepository extends ApplicationMongoRepository<Account> {
    Optional<Account> findByMailAddress(String mailAddress);
}
