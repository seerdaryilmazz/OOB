package ekol.crm.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.account.domain.dto.AccountJson;
import ekol.crm.account.domain.dto.AccountMergeJson;
import ekol.crm.account.domain.dto.ContactJson;
import ekol.crm.account.domain.dto.potential.PotentialJson;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.event.dto.AccountEventMessage;
import ekol.crm.account.event.dto.Operation;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccountMergeService {
	
	private AccountCrudService accountCrudService;
	private ContactCrudService contactCrudService;
	private PotentialCrudService potentialCrudService;
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Transactional
	public Account merge(Long accountId, Long otherAccountId, AccountMergeJson accountMergeJson) {
		Account account = accountCrudService.getByIdOrThrowException(accountId);
		Account otherAccount = accountCrudService.getByIdOrThrowException(otherAccountId);
		accountMergeJson.setDeletedAccount(AccountJson.fromEntity(otherAccount));
		deleteAccount(otherAccount);
		applicationEventPublisher.publishEvent(AccountEventMessage.with(accountMergeJson, Operation.ACCOUNT_MERGE));
		return save(account, accountMergeJson);
	}
    
    private void deleteAccount(Account account){
        accountCrudService.forceDelete(account);
        contactCrudService.getByAccountId(account.getId()).forEach(contactCrudService::delete);
        potentialCrudService.getByAccountId(account.getId()).stream().forEach(potentialCrudService::delete);
    }
    
    private Account save(Account account, AccountMergeJson accountMergeJson){
       
    	Account savedAccount = accountMergeJson.getAccount().toEntity();
    	accountCrudService.save(savedAccount);
    	accountMergeJson.getPotentials().stream().map(PotentialJson::toEntity).forEach(potential->{
    		potentialCrudService.save(savedAccount.getId(), potential);
    	});
    	applicationEventPublisher.publishEvent(accountMergeJson);
   
        return savedAccount;
    }
}
