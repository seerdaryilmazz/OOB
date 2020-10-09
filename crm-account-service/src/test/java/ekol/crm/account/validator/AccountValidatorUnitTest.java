package ekol.crm.account.validator;


import ekol.crm.account.domain.model.Account;
import ekol.crm.account.mockdata.MockData;
import ekol.crm.account.repository.AccountRepository;
import ekol.exceptions.ValidationException;
import ekol.crm.account.domain.model.IdNamePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


@RunWith(SpringRunner.class)
public class AccountValidatorUnitTest {

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void shouldNotThrowExceptionForValidEntity() {

        Account account = MockData.ekolSpainAccount.build();

        AccountValidator validator = new AccountValidator(accountRepository);

        given(accountRepository.findByCompanyId(account.getCompany().getId())).willReturn(Optional.empty());

        validator.validate(account);

        then(accountRepository).should(times(1)).findByCompanyId(account.getCompany().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForExistingCompanyAccount() {

        Account account = MockData.ekolSpainAccount.build();

        AccountValidator validator = new AccountValidator(accountRepository);

        given(accountRepository.findByCompanyId(account.getCompany().getId())).willReturn(Optional.of(account));

        validator.validate(account);

        then(accountRepository).should(times(1)).findByCompanyId(account.getCompany().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyCompany() {

        Account account = MockData.ekolSpainAccount.but().withCompany(null).build();

        AccountValidator validator = new AccountValidator(accountRepository);

        validator.validate(account);

        then(accountRepository).should(never()).findByCompanyId(account.getCompany().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyCompanyId() {

        Account account = MockData.ekolSpainAccount.but().withCompany(new IdNamePair(null, "EKOL SPAIN")).build();

        AccountValidator validator = new AccountValidator(accountRepository);

        validator.validate(account);

        then(accountRepository).should(never()).findByCompanyId(account.getCompany().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyCompanyName() {

        Account account = MockData.ekolSpainAccount.but().withCompany(new IdNamePair(1L, "")).build();

        AccountValidator validator = new AccountValidator(accountRepository);

        validator.validate(account);

        then(accountRepository).should(never()).findByCompanyId(account.getCompany().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyAccountOwner() {

        Account account = MockData.ekolSpainAccount.but().withAccountOwner("").build();

        AccountValidator validator = new AccountValidator(accountRepository);

        validator.validate(account);

        then(accountRepository).should(never()).findByCompanyId(account.getCompany().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyCountryCode() {

        Account account = MockData.ekolSpainAccount.but().withCountry(null).build();

        AccountValidator validator = new AccountValidator(accountRepository);

        validator.validate(account);

        then(accountRepository).should(never()).findByCompanyId(account.getCompany().getId());

    }



}
