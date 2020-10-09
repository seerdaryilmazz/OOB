package ekol.crm.account.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AccountCrudServiceUnitTest {

    @Test
    public void shouldSave() {

    }

//    @MockBean
//    private AccountRepository repository;
//
//    @MockBean
//    private AccountValidator validator;
//
//    @MockBean
//    private ApplicationEventPublisher applicationEventPublisher;
//
//    @Test
//    public void shouldValidateAndSaveAccount() {
//
//        Account account = MockData.ekolSpainAccount.build();
//        AccountCrudService accountCrudService = new AccountCrudService(repository, validator, applicationEventPublisher);
//
//        given(repository.save(any(Account.class))).willReturn(account.toBuilder().id(1L).build());
//        doNothing().when(applicationEventPublisher).publishEvent(any(Account.class));
//
//        Account response = accountCrudService.save(account);
//
//        assertEquals(response.getId(), new Long(1));
//        assertEquals(response.getName(), account.getName());
//        assertEquals(response.getCompany(), account.getCompany());
//        assertEquals(response.getAccountType(), account.getAccountType());
//        assertEquals(response.getAccountOwner(), account.getAccountOwner());
//        assertEquals(response.getCountry().getIso(), account.getCountry().getIso());
//        then(validator).should(times(1)).validate(account);
//        then(repository).should(times(1)).save(account);
//    }
//
//    @Test(expected = ValidationException.class)
//    public void shouldNotSaveAccountWhenValidationFails() {
//
//        Account account = MockData.ekolSpainAccount.but().withCompany(null).build();
//        AccountCrudService accountCrudService = new AccountCrudService(repository, validator, applicationEventPublisher);
//
//        willThrow(new ValidationException("validation exception")).given(validator).validate(account);
//
//        accountCrudService.save(account);
//
//        then(validator).should(times(1)).validate(account);
//        then(repository).should(never()).save(account);
//    }
//
//    @Test
//    public void shouldGetAccountWhenIdDoesExist(){
//
//        Account account = MockData.ekolSpainAccount.build();
//
//        AccountCrudService accountCrudService = new AccountCrudService(repository, validator, applicationEventPublisher);
//        final Long id = 1L;
//
//        given(repository.findById(id)).willReturn(Optional.of(account));
//
//        Account response = accountCrudService.getByIdOrThrowException(id);
//
//        assertEquals(account.getId(), response.getId());
//
//        then(repository).should(times(1)).findById(id);
//    }
//
//    @Test
//    public void shouldGetAccountWhenCompanyIdDoesExist(){
//
//        Account account = MockData.ekolSpainAccount.build();
//
//        AccountCrudService accountCrudService = new AccountCrudService(repository, validator, applicationEventPublisher);
//        final Long companyId = 1L;
//
//        given(repository.findByCompanyId(companyId)).willReturn(Optional.of(account));
//
//        Account response = accountCrudService.getByCompanyIdOrThrowException(companyId);
//
//        assertEquals(account.getId(), response.getId());
//
//        then(repository).should(times(1)).findByCompanyId(companyId);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void shouldThrowResourceNotFoundWhenIdDoesNotExist(){
//
//        AccountCrudService accountCrudService = new AccountCrudService(repository, validator, applicationEventPublisher);
//        final Long id = 3L;
//
//        given(repository.findById(id)).willReturn(Optional.empty());
//
//        accountCrudService.getByIdOrThrowException(id);
//
//        then(repository).should(times(1)).findById(id);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void shouldThrowResourceNotFoundWhenCompanyIdDoesNotExist(){
//
//        AccountCrudService accountCrudService = new AccountCrudService(repository, validator, applicationEventPublisher);
//        final Long companyId = 3L;
//
//        given(repository.findByCompanyId(companyId)).willReturn(Optional.empty());
//
//        accountCrudService.getByCompanyIdOrThrowException(companyId);
//
//        then(repository).should(times(1)).findByCompanyId(companyId);
//    }
}
