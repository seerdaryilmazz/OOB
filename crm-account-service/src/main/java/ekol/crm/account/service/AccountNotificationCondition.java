package ekol.crm.account.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.crm.account.domain.dto.AccountJson;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.repository.AccountRepository;

@Component
public class AccountNotificationCondition {
	
	@Autowired
	private AccountRepository accountRepository;
	   
	public boolean isOwnerChanged(AccountJson accountJson) {
		String accountOwner = accountRepository.findById(accountJson.getId()).map(Account::getAccountOwner).orElse(null);
		return !Objects.equals(accountOwner, accountJson.getAccountOwner());
	}

}
