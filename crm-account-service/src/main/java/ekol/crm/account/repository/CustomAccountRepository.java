package ekol.crm.account.repository;

import ekol.crm.account.domain.model.Account;

public interface CustomAccountRepository {
	void detach(Account account);
}
