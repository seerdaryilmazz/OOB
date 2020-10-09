package ekol.crm.account.repository;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import ekol.crm.account.domain.model.Account;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface AccountRepository extends ApplicationRepository<Account>, JpaSpecificationExecutor<Account> {

    Optional<Account> findById(Long id);
    Optional<Account> findByCompanyId(Long companyId);
    List<Account> findByAccountOwner(String accountOwner);
    List<Account> findByAccountOwnerIn(List<String> accountOwners);

    @Query("select a from Account a where a.details.globalAccountId= :globalAccountId")
    Page<Account> findByGlobalAccountId(@Param("globalAccountId") Long globalAccountId, Pageable pageable);

    @Query("select a from Account a where a.details.globalAccountId=:id")
    List<Account> findAllByGlobalAccountId(@Param("id") Long id);

}
