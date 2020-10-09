package ekol.crm.account.validator;

import ekol.crm.account.builder.AccountBuilder;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.crm.account.mockdata.MockData;
import ekol.crm.account.repository.AccountRepository;
import ekol.exceptions.ValidationException;
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
public class PotentialValidatorUnitTest {

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void shouldNotThrowExceptionForValidEntity() {

        Account account = AccountBuilder.anAccount().but().withId(1L).build();

        Potential potential = MockData.ekolSpainPotential.but().withAccount(account).build();

        PotentialValidator validator = new PotentialValidator(accountRepository);

        given(accountRepository.findById(potential.getAccount().getId())).willReturn(Optional.of(account));

        validator.validate(potential);

        then(accountRepository).should(times(1)).findById(potential.getAccount().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyAccount() {

        Potential potential = MockData.ekolSpainPotential.but().withAccount(new Account()).build();

        PotentialValidator validator = new PotentialValidator(accountRepository);

        validator.validate(potential);

        then(accountRepository).should(never()).findById(potential.getAccount().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForMissingAccount() {

        Potential potential = MockData.ekolSpainPotential.but().withAccount(null).build();

        PotentialValidator validator = new PotentialValidator(accountRepository);

        validator.validate(potential);

        then(accountRepository).should(times(1)).findById(potential.getAccount().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyFromCountryCode() {

        Potential potential = MockData.ekolSpainPotential.but().withFromCountry(null).build();
        potential.setFromCountry(null);

        PotentialValidator validator = new PotentialValidator(accountRepository);

        validator.validate(potential);

        then(accountRepository).should(never()).findById(potential.getAccount().getId());

    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyToCountryCode() {

        Potential potential = MockData.ekolSpainPotential.but().withToCountry(null).build();

        PotentialValidator validator = new PotentialValidator(accountRepository);

        validator.validate(potential);

        then(accountRepository).should(never()).findById(potential.getAccount().getId());

    }


    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForEmptyFrequency() {

        Potential potential = MockData.ekolSpainPotential.but().withFrequency(null).build();

        PotentialValidator validator = new PotentialValidator(accountRepository);

        validator.validate(potential);

        then(accountRepository).should(never()).findById(potential.getAccount().getId());

    }

}
