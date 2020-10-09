package ekol.crm.account.event;


import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.crm.account.domain.dto.AccountJson;
import ekol.crm.account.domain.dto.AccountMergeJson;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.event.dto.AccountEventMessage;
import ekol.event.annotation.ProducesEvent;

@Component
public class AccountUpdateEventProducer {

    @ProducesEvent(event = "account-update")
    @TransactionalEventListener(fallbackExecution = true)
    public AccountJson produce(Account account) {
        return AccountJson.fromEntity(account);
    }

    @ProducesEvent(event = "account-owner-update")
    @TransactionalEventListener(fallbackExecution = true, condition = "#accountEventMessage.operation == T(ekol.crm.account.event.dto.Operation).ACCOUNT_OWNER_CHANGED")
    public AccountJson produceAccountOwnerChanged(AccountEventMessage accountEventMessage) {
        return AccountJson.fromEntity(accountEventMessage.getData(Account.class));
    }
    
    @ProducesEvent(event = "account-delete")
    @TransactionalEventListener(fallbackExecution = true, condition = "#accountEventMessage.operation == T(ekol.crm.account.event.dto.Operation).ACCOUNT_DELETE")
    public AccountJson produceAccountDelete(AccountEventMessage accountEventMessage) {
    	return AccountJson.fromEntity(accountEventMessage.getData(Account.class));
    }
    
    @ProducesEvent(event = "account-merge")
    @TransactionalEventListener(fallbackExecution = true, condition = "#accountEventMessage.operation == T(ekol.crm.account.event.dto.Operation).ACCOUNT_MERGE")
    public AccountMergeJson produceAccountMerge(AccountEventMessage accountEventMessage) {
    	return accountEventMessage.getData(AccountMergeJson.class);
    }
}
