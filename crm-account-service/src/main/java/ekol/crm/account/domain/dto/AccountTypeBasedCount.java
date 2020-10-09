package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.enumaration.AccountType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountTypeBasedCount {

    private AccountType accountType;

    private Long count;

    public AccountTypeBasedCount() {
    }

    public AccountTypeBasedCount(AccountType accountType, Long count) {
        this.accountType = accountType;
        this.count = count;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
