package ekol.crm.account.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class PotentialCrudServiceUnitTest {

    @Test
    public void shouldSave() {

    }


//    @MockBean
//    private PotentialRepository repository;
//
//    @MockBean
//    private PotentialValidator validator;
//
//    @MockBean
//    private AccountCrudService accountService;
//
//    @MockBean
//    private PotentialCountryPointCrudService potentialCountryPointCrudService;
//
//
//    @Test
//    public void shouldValidateAndSavePotential() {
//
//        Account account = AccountBuilder.anAccount().but().withId(1L).build();
//
//        Potential potential = MockData.ekolSpainPotential.but().withAccount(account).build();
//
//        PotentialCrudService potentialCrudService = new PotentialCrudService(repository, validator, accountService, potentialCountryPointCrudService);
//
//        given(accountService.getByIdOrThrowException(any(Long.class))).willReturn(account);
//        given(repository.findById(any(Long.class))).willReturn(Optional.of(potential));
//        given(repository.save(any(Potential.class))).willReturn(potential);
//
//        Potential response = potentialCrudService.save(potential.getAccount().getId(), potential);
//
//        assertEquals(response.getId(), new Long(1));
//        assertEquals(response.getAccount().getId(), potential.getAccount().getId());
//        assertEquals(response.getFromCountry().getIso(), potential.getFromCountry().getIso());
//        assertEquals(response.getToCountry().getIso(), potential.getToCountry().getIso());
//        assertEquals(response.getShipmentLoadingType(), potential.getShipmentLoadingType());
//        assertEquals(response.getFrequencyType(), potential.getFrequencyType());
//        assertEquals(response.getFrequency(), potential.getFrequency());
//        then(validator).should(times(1)).validate(potential);
//        then(repository).should(times(1)).save(potential);
//    }
//
//    @Test(expected = ValidationException.class)
//    public void shouldNotSavePotentialWhenValidationFails() {
//
//        RoadPotential potential = MockData.ekolSpainPotential.but().withFromCountry(null).build();
//
//        final Long accountId = 1L;
//        PotentialCrudService potentialCrudService = new PotentialCrudService(repository, validator, accountService, potentialCountryPointCrudService);
//
//        willThrow(new ValidationException("validation exception")).given(validator).validate(potential);
//
//        potentialCrudService.save(accountId, potential);
//
//        then(validator).should(times(1)).validate(potential);
//        then(repository).should(never()).save(potential);
//    }
//
//
//    @Test
//    public void shouldGetPotentialWhenAccountIdDoesExist(){
//
//        RoadPotential potential = MockData.ekolSpainPotential.build();
//        PotentialCrudService potentialCrudService = new PotentialCrudService(repository, validator, accountService, potentialCountryPointCrudService);
//        final Long accountId = 1L;
//
//        given(repository.findByAccountIdAndStatus(accountId, PotentialStatus.ACTIVE)).willReturn(Arrays.asList(potential));
//
//        List<Potential> response = potentialCrudService.getByAccountId(accountId);
//
//        assertEquals(1, response.size());
//
//        then(repository).should(times(1)).findByAccountIdAndStatus(accountId, PotentialStatus.ACTIVE);
//
//    }
//
//    @Test
//    public void shouldGetPotentialWhenIdDoesExist(){
//
//        RoadPotential potential = MockData.ekolSpainPotential.build();
//
//        PotentialCrudService potentialCrudService = new PotentialCrudService(repository, validator, accountService, potentialCountryPointCrudService);
//        final Long id = 1L;
//
//        given(repository.findById(id)).willReturn(Optional.of(potential));
//
//        Potential response = potentialCrudService.getByIdOrThrowException(id);
//
//        assertEquals(potential.getId(), response.getId());
//
//        then(repository).should(times(1)).findById(id);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void shouldThrowResourceNotFoundWhenIdDoesNotExist(){
//
//        PotentialCrudService potentialCrudService = new PotentialCrudService(repository, validator, accountService, potentialCountryPointCrudService);
//        final Long id = 1L;
//
//        given(repository.findById(id)).willReturn(Optional.empty());
//
//        potentialCrudService.getByIdOrThrowException(id);
//
//        then(repository).should(times(1)).findById(id);
//    }


}
