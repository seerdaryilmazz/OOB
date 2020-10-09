package ekol.usermgr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;

import ekol.usermgr.domain.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long>, JpaSpecificationExecutor<UserLogin> {
	@Query("SELECT DISTINCT clientId FROM #{#entityName}")
	List<String> findDistinctClientId();
	
	@Query("SELECT DISTINCT clientId FROM #{#entityName} WHERE username = ?1")
	List<String> findDistinctClientIdByUsername(String username);
}
