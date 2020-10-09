package ekol.crm.account.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ekol.crm.account.domain.model.Account;

@Repository
public class CustomAccountRepositoryImpl implements CustomAccountRepository {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public void detach(Account account) {
		entityManager.detach(account);
	}
}
