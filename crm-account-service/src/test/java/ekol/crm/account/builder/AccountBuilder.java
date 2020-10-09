package ekol.crm.account.builder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.enumaration.AccountType;
import ekol.crm.account.domain.enumaration.SegmentType;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.domain.model.CodeNamePair;
import ekol.crm.account.domain.model.IsoNamePair;
import ekol.crm.account.domain.model.IdNamePair;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public final class AccountBuilder {
    private boolean deleted;
    private Long id;
    private String name;
    private IdNamePair company;
    private IsoNamePair country;
    private String accountOwner;
    private AccountType accountType;
    private SegmentType segment;
    private CodeNamePair parentSector;
    private CodeNamePair subSector;


    public static AccountBuilder anAccount() {
        return new AccountBuilder();
    }

    public AccountBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public AccountBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AccountBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AccountBuilder withCompany(IdNamePair company) {
        this.company = company;
        return this;
    }

    public AccountBuilder withCountry(IsoNamePair country) {
        this.country = country;
        return this;
    }

    public AccountBuilder withAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
        return this;
    }

    public AccountBuilder withAccountType(AccountType accountType) {
        this.accountType = accountType;
        return this;
    }

    public AccountBuilder withSegmentType(SegmentType segment) {
        this.segment = segment;
        return this;
    }

    public AccountBuilder withParentSector(CodeNamePair parentSector) {
        this.parentSector = parentSector;
        return this;
    }

    public AccountBuilder withSubSector(CodeNamePair subSector) {
        this.subSector = subSector;
        return this;
    }



    public AccountBuilder but() {
        return anAccount()
                .withDeleted(deleted)
                .withId(id)
                .withName(name)
                .withCompany(company)
                .withCountry(country)
                .withAccountOwner(accountOwner)
                .withAccountType(accountType)
                .withSegmentType(segment)
                .withParentSector(parentSector)
                .withSubSector(subSector);
    }

    public Account build() {
        Account account = new Account();
        account.setDeleted(deleted);
        account.setId(id);
        account.setName(name);
        account.setCompany(company);
        account.setCountry(country);
        account.setAccountOwner(accountOwner);
        account.setAccountType(accountType);
        account.setSegment(segment);
        account.setParentSector(parentSector);
        account.setSubSector(subSector);
        return account;
    }
}
