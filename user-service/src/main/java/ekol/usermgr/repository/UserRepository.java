package ekol.usermgr.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.usermgr.domain.*;

/**
 * Created by kilimci on 13/04/16.
 */
public interface UserRepository extends ApplicationRepository<User>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    User findByUsernameAndStatus(String username, UserStatus status);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndStatus(String username, UserStatus status);
    User findByEmail(String email);
    User findByEmailAndStatus(String email, UserStatus status);
    Iterable<User> findByUsernameIn(Iterable<String> username);
    Iterable<User> findByUsernameInAndStatusInAndAuthenticationTypeIn(Iterable<String> username, Iterable<UserStatus> status,  Iterable<UserAuthenticationType> authenticationType);
}
