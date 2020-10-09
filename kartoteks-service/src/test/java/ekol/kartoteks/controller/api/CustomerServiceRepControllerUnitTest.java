package ekol.kartoteks.controller.api;

import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.embeddable.DateWindow;
import ekol.kartoteks.builder.*;
import ekol.kartoteks.controller.CustomerServiceRepController;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.dto.CustomerServiceRepAuthorizedCompany;
import ekol.kartoteks.repository.CompanyEmployeeRelationRepository;
import ekol.kartoteks.repository.CompanyRoleRepository;
import ekol.kartoteks.repository.CompanyRoleTypeRepository;
import ekol.kartoteks.repository.EmployeeCustomerRelationRepository;
import ekol.kartoteks.service.UserService;
import ekol.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by kilimci on 20/09/16.
 */
@RunWith(SpringRunner.class)
public class CustomerServiceRepControllerUnitTest {


    @Mock
    private CompanyEmployeeRelationRepository companyEmployeeRelationRepository;

    @Mock
    private EmployeeCustomerRelationRepository employeeCustomerRelationRepository;

    @Mock
    private CompanyRoleRepository companyRoleRepository;

    @Mock
    private CompanyRoleTypeRepository companyRoleTypeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomerServiceRepController customerServiceRepController;

    private EmployeeCustomerRelation customerServiceRep = new EmployeeCustomerRelation();
    private User user = new User();
    private CompanyEmployeeRelation companyEmployeeRelation = new CompanyEmployeeRelation();
    private CompanyRole companyRole = new CompanyRole();
    private Company company = new Company();
    private CustomerServiceRepAuthorizedCompany correctResult = new CustomerServiceRepAuthorizedCompany();
    private LocalDate now = LocalDate.now();

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);


        user.setUsername("accountName");
        company.setId(1L);
        companyRole.setCompany(company);
        companyRole.setDateRange(new DateWindow(now.minus(1, ChronoUnit.DAYS), now.plus(1, ChronoUnit.DAYS)));
        companyEmployeeRelation.setId(1L);
        companyEmployeeRelation.setCompanyRole(companyRole);

        correctResult.setCompany(company);
        correctResult.setRelationId(companyEmployeeRelation.getId());
        correctResult.setValidDates(companyRole.getDateRange());

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private void expectException(String message){
        thrown.expect(ResourceNotFoundException.class);
        thrown.expectMessage(message);
    }


    private void checkAuthorizedCompanyEquals(Set<CustomerServiceRepAuthorizedCompany> result, CustomerServiceRepAuthorizedCompany correctResult){
        List<CustomerServiceRepAuthorizedCompany> list = new ArrayList<>(result);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getCompany(), correctResult.getCompany());
        assertEquals(list.get(0).getRelationId(), correctResult.getRelationId());
        assertEquals(list.get(0).getValidDates().getStartDate(), correctResult.getValidDates().getStartDate());
        assertEquals(list.get(0).getValidDates().getEndDate(), correctResult.getValidDates().getEndDate());
    }

    @Test
    public void shouldFindCustomerRepCompanies(){
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);
        when(userService.getSessionUser()).thenReturn(user);
        when(userService.getUserByAccountName(user.getUsername())).thenReturn(user);
        when(companyEmployeeRelationRepository.findByEmployeeAccountAndRelation(user.getUsername(), customerServiceRep))
                .thenReturn(Arrays.asList(companyEmployeeRelation));

        checkAuthorizedCompanyEquals(customerServiceRepController.findCompaniesOfSessionCustomerServiceRep(), correctResult);
        checkAuthorizedCompanyEquals(customerServiceRepController.findCompaniesOfCustomerServiceRep(user.getUsername()), correctResult);
    }

    @Test
    public void shouldOnlyFindCurrentCustomerRepCompanies(){
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);
        when(userService.getSessionUser()).thenReturn(user);
        when(userService.getUserByAccountName(user.getUsername())).thenReturn(user);
        CompanyEmployeeRelation oldCompanyEmployeeRelation = new CompanyEmployeeRelation();

        Company otherCompany = new Company();
        otherCompany.setId(2L);
        CompanyRole oldCompanyRole = new CompanyRole();
        oldCompanyRole.setDateRange(new DateWindow(LocalDate.now().minus(2, ChronoUnit.DAYS), LocalDate.now().minus(1, ChronoUnit.DAYS)));
        oldCompanyRole.setCompany(otherCompany);
        oldCompanyEmployeeRelation.setCompanyRole(oldCompanyRole);

        when(companyEmployeeRelationRepository.findByEmployeeAccountAndRelation(user.getUsername(), customerServiceRep))
                .thenReturn(Arrays.asList(companyEmployeeRelation, oldCompanyEmployeeRelation));

        checkAuthorizedCompanyEquals(customerServiceRepController.findCompaniesOfSessionCustomerServiceRep(), correctResult);
        checkAuthorizedCompanyEquals(customerServiceRepController.findCompaniesOfCustomerServiceRep(user.getUsername()), correctResult);

    }
    @Test
    public void shouldCheckRelationValidDatesIfExists(){
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);
        when(userService.getSessionUser()).thenReturn(user);
        when(userService.getUserByAccountName(user.getUsername())).thenReturn(user);

        CompanyEmployeeRelation outOfDateCompanyEmployeeRelation = new CompanyEmployeeRelation();
        Company company = new Company();
        company.setId(1L);
        CompanyRole validCompanyRole = new CompanyRole();
        validCompanyRole.setDateRange(new DateWindow(LocalDate.now().minus(1, ChronoUnit.MONTHS), LocalDate.now().plus(1, ChronoUnit.MONTHS)));
        validCompanyRole.setCompany(company);
        outOfDateCompanyEmployeeRelation.setCompanyRole(validCompanyRole);
        outOfDateCompanyEmployeeRelation.setValidDates(new DateWindow(LocalDate.now().minus(2, ChronoUnit.DAYS), LocalDate.now().minus(1, ChronoUnit.DAYS)));

        CompanyEmployeeRelation tempCompanyEmployeeRelation = new CompanyEmployeeRelation();
        Company companyToReturn = new Company();
        companyToReturn.setId(2L);
        validCompanyRole = new CompanyRole();
        validCompanyRole.setDateRange(new DateWindow(LocalDate.now().minus(2, ChronoUnit.MONTHS), LocalDate.now().plus(2, ChronoUnit.MONTHS)));
        validCompanyRole.setCompany(companyToReturn);
        tempCompanyEmployeeRelation.setCompanyRole(validCompanyRole);
        tempCompanyEmployeeRelation.setValidDates(new DateWindow(LocalDate.now().minus(1, ChronoUnit.DAYS), LocalDate.now().plus(1, ChronoUnit.DAYS)));

        when(companyEmployeeRelationRepository.findByEmployeeAccountAndRelation(user.getUsername(), customerServiceRep))
                .thenReturn(Arrays.asList(outOfDateCompanyEmployeeRelation, tempCompanyEmployeeRelation));

        checkAuthorizedCompanyEquals(customerServiceRepController.findCompaniesOfSessionCustomerServiceRep(),
                CustomerServiceRepAuthorizedCompany.withCompanyEmployeeRelation(tempCompanyEmployeeRelation));
        checkAuthorizedCompanyEquals(customerServiceRepController.findCompaniesOfCustomerServiceRep(user.getUsername()),
                CustomerServiceRepAuthorizedCompany.withCompanyEmployeeRelation(tempCompanyEmployeeRelation));


    }

    @Test
    public void shouldHandleMissingCustomerRelationWithSessionUser(){
        expectException("Customer service representative relation does not exist");
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(null);
        when(userService.getSessionUser()).thenReturn(user);
        when(companyEmployeeRelationRepository.findByEmployeeAccountAndRelation(user.getUsername(), customerServiceRep))
                .thenReturn(Arrays.asList(companyEmployeeRelation));
        customerServiceRepController.findCompaniesOfSessionCustomerServiceRep();
    }
    @Test
    public void shouldHandleMissingCustomerRelationWithAccountName(){
        expectException("Customer service representative relation does not exist");
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(null);
        when(userService.getUserByAccountName(user.getUsername())).thenReturn(user);
        when(companyEmployeeRelationRepository.findByEmployeeAccountAndRelation(user.getUsername(), customerServiceRep))
                .thenReturn(Arrays.asList(companyEmployeeRelation));
        customerServiceRepController.findCompaniesOfCustomerServiceRep(user.getUsername());
    }
    @Test
    public void shouldHandleMissingSessionUser(){
        expectException("Session user does not exist");
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);
        when(userService.getSessionUser()).thenReturn(null);
        when(companyEmployeeRelationRepository.findByEmployeeAccountAndRelation(user.getUsername(), customerServiceRep))
                .thenReturn(Arrays.asList(companyEmployeeRelation));

        customerServiceRepController.findCompaniesOfSessionCustomerServiceRep();
    }
    @Test
    public void shouldHandleMissingAccountName(){
        expectException("User account accountName does not exist");
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);
        when(userService.getUserByAccountName(user.getUsername())).thenReturn(null);
        when(companyEmployeeRelationRepository.findByEmployeeAccountAndRelation(user.getUsername(), customerServiceRep))
                .thenReturn(Arrays.asList(companyEmployeeRelation));
        customerServiceRepController.findCompaniesOfCustomerServiceRep(user.getUsername());

    }

    @Test
    public void shouldDeleteCompanyRelation() {
        Long id = 1L;
        CompanyEmployeeRelation relation = CompanyEmployeeRelationBuilder.aCompanyEmployeeRelation().withId(1L).build();
        when(companyEmployeeRelationRepository.findOne(id)).thenReturn(relation);
        ArgumentCaptor<CompanyEmployeeRelation> argumentCaptor = ArgumentCaptor.forClass(CompanyEmployeeRelation.class);
        customerServiceRepController.deleteCompanyRelation(id);

        verify(companyEmployeeRelationRepository).save(argumentCaptor.capture());

        assertEquals(id, argumentCaptor.getValue().getId());
        assertTrue(argumentCaptor.getValue().isDeleted());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenDeleteCompanyRelationIdNotFound() {
        Long id = 1L;
        CompanyEmployeeRelation relation = CompanyEmployeeRelationBuilder.aCompanyEmployeeRelation().withId(1L).build();
        when(companyEmployeeRelationRepository.findOne(id)).thenReturn(null);
        customerServiceRepController.deleteCompanyRelation(id);
        verify(companyEmployeeRelationRepository, times(0)).save(relation);
    }

    @Test
    public void shouldSaveCompanyRelation() {
        Long companyId = 1L;
        Long companyRoleId = 2L;
        Long companyEmployeeRelationId = 4L;
        String employeeAccount = "account";
        DateWindow dateWindow = new DateWindow(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Company company = CompanyBuilder.aCompany()
                .withName("name").withId(companyId).build();
        CompanyRoleType roleType = CompanyRoleTypeBuilder.aCompanyRoleType()
                .withCode("CUSTOMER").withId(1L).build();
        CompanyRole companyRole = CompanyRoleBuilder.aCompanyRole()
                .withCompany(company).withId(companyRoleId).withRoleType(roleType).build();
        CompanyEmployeeRelation relation = CompanyEmployeeRelationBuilder.aCompanyEmployeeRelation()
                .withId(companyEmployeeRelationId).withCompanyRole(companyRole)
                .withValidDates(dateWindow).build();


        CustomerServiceRepAuthorizedCompany authorizedCompany = CustomerServiceRepAuthorizedCompany.withCompanyEmployeeRelation(relation);

        when(companyRoleTypeRepository.findByCode("CUSTOMER")).thenReturn(roleType);
        when(companyRoleRepository.findByCompanyId(companyId)).thenReturn(Arrays.asList(companyRole));
        when(companyEmployeeRelationRepository.findOne(companyEmployeeRelationId)).thenReturn(relation);

        ArgumentCaptor<CompanyEmployeeRelation> argumentCaptor = ArgumentCaptor.forClass(CompanyEmployeeRelation.class);

        customerServiceRepController.saveCompanyRelation(employeeAccount, authorizedCompany);

        verify(companyEmployeeRelationRepository).save(argumentCaptor.capture());

        assertEquals(employeeAccount, argumentCaptor.getValue().getEmployeeAccount());
        assertEquals(companyEmployeeRelationId, argumentCaptor.getValue().getId());
        assertEquals(dateWindow, argumentCaptor.getValue().getValidDates());
        assertEquals(companyRoleId, argumentCaptor.getValue().getCompanyRole().getId());
    }

    @Test
    public void shouldCreateNewRelationSaveCompanyRelation() {
        Long companyId = 1L;
        Long companyRoleId = 2L;
        Long customerRelationId = 3L;
        Long companyEmployeeRelationId = 4L;
        String employeeAccount = "account";
        DateWindow dateWindow = new DateWindow(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Company company = CompanyBuilder.aCompany()
                .withName("name").withId(companyId).build();
        CompanyRoleType roleType = CompanyRoleTypeBuilder.aCompanyRoleType()
                .withCode("CUSTOMER").withId(companyRoleId).build();
        CompanyRole companyRole = CompanyRoleBuilder.aCompanyRole()
                .withCompany(company).withId(companyRoleId).withRoleType(roleType).build();
        CompanyEmployeeRelation relation = CompanyEmployeeRelationBuilder.aCompanyEmployeeRelation()
                .withCompanyRole(companyRole)
                .withValidDates(dateWindow).build();
        EmployeeCustomerRelation customerServiceRep = EmployeeCustomerRelationBuilder.anEmployeeCustomerRelation()
                .withCode("CUSTOMER_SERVICE_REP").withId(customerRelationId).build();


        CustomerServiceRepAuthorizedCompany authorizedCompany = CustomerServiceRepAuthorizedCompany.withCompanyEmployeeRelation(relation);

        when(companyRoleTypeRepository.findByCode("CUSTOMER")).thenReturn(roleType);
        when(companyRoleRepository.findByCompanyId(companyId)).thenReturn(Arrays.asList(companyRole));
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);

        ArgumentCaptor<CompanyEmployeeRelation> argumentCaptor = ArgumentCaptor.forClass(CompanyEmployeeRelation.class);

        customerServiceRepController.saveCompanyRelation(employeeAccount, authorizedCompany);

        verify(companyEmployeeRelationRepository, times(0)).findOne(companyEmployeeRelationId);
        verify(companyEmployeeRelationRepository).save(argumentCaptor.capture());

        assertEquals(employeeAccount, argumentCaptor.getValue().getEmployeeAccount());
        assertEquals(dateWindow, argumentCaptor.getValue().getValidDates());
        assertEquals(companyRoleId, argumentCaptor.getValue().getCompanyRole().getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenCompanyRoleDoesNotExist() {
        Long companyId = 1L;
        Long companyRoleId = 2L;
        Long customerRelationId = 3L;
        String employeeAccount = "account";
        DateWindow dateWindow = new DateWindow(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Company company = CompanyBuilder.aCompany()
                .withName("name").withId(companyId).build();
        CompanyRoleType roleType = CompanyRoleTypeBuilder.aCompanyRoleType()
                .withCode("CUSTOMER").withId(companyRoleId).build();
        CompanyRole companyRole = CompanyRoleBuilder.aCompanyRole()
                .withCompany(company).withId(companyRoleId).withRoleType(roleType).build();
        CompanyEmployeeRelation relation = CompanyEmployeeRelationBuilder.aCompanyEmployeeRelation()
                .withCompanyRole(companyRole)
                .withValidDates(dateWindow).build();
        EmployeeCustomerRelation customerServiceRep = EmployeeCustomerRelationBuilder.anEmployeeCustomerRelation()
                .withCode("CUSTOMER_SERVICE_REP").withId(customerRelationId).build();


        CustomerServiceRepAuthorizedCompany authorizedCompany = CustomerServiceRepAuthorizedCompany.withCompanyEmployeeRelation(relation);

        when(companyRoleTypeRepository.findByCode("CUSTOMER")).thenReturn(roleType);
        when(companyRoleRepository.findByCompanyId(companyId)).thenReturn(new ArrayList<>());
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);

        ArgumentCaptor<CompanyEmployeeRelation> argumentCaptor = ArgumentCaptor.forClass(CompanyEmployeeRelation.class);

        customerServiceRepController.saveCompanyRelation(employeeAccount, authorizedCompany);

        verify(companyEmployeeRelationRepository, times(0)).save(argumentCaptor.capture());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenCompanyRoleTypeDoesNotExist() {
        Long companyId = 1L;
        Long companyRoleId = 2L;
        Long customerRelationId = 3L;
        String employeeAccount = "account";
        DateWindow dateWindow = new DateWindow(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Company company = CompanyBuilder.aCompany()
                .withName("name").withId(companyId).build();
        CompanyRoleType roleType = CompanyRoleTypeBuilder.aCompanyRoleType()
                .withCode("CUSTOMER").withId(companyRoleId).build();
        CompanyRole companyRole = CompanyRoleBuilder.aCompanyRole()
                .withCompany(company).withId(companyRoleId).withRoleType(roleType).build();
        CompanyEmployeeRelation relation = CompanyEmployeeRelationBuilder.aCompanyEmployeeRelation()
                .withCompanyRole(companyRole)
                .withValidDates(dateWindow).build();
        EmployeeCustomerRelation customerServiceRep = EmployeeCustomerRelationBuilder.anEmployeeCustomerRelation()
                .withCode("CUSTOMER_SERVICE_REP").withId(customerRelationId).build();


        CustomerServiceRepAuthorizedCompany authorizedCompany = CustomerServiceRepAuthorizedCompany.withCompanyEmployeeRelation(relation);

        when(companyRoleTypeRepository.findByCode("CUSTOMER")).thenReturn(null);
        when(companyRoleRepository.findByCompanyId(companyId)).thenReturn(Arrays.asList(companyRole));
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);

        ArgumentCaptor<CompanyEmployeeRelation> argumentCaptor = ArgumentCaptor.forClass(CompanyEmployeeRelation.class);

        customerServiceRepController.saveCompanyRelation(employeeAccount, authorizedCompany);

        verify(companyEmployeeRelationRepository, times(0)).save(argumentCaptor.capture());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenCompanyRoleTypeCustomerDoesNotExist() {
        Long companyId = 1L;
        Long companyRoleId = 2L;
        Long customerRelationId = 3L;
        String employeeAccount = "account";
        DateWindow dateWindow = new DateWindow(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Company company = CompanyBuilder.aCompany()
                .withName("name").withId(companyId).build();
        CompanyRoleType customer = CompanyRoleTypeBuilder.aCompanyRoleType()
                .withCode("CUSTOMER").withId(companyRoleId).build();
        CompanyRoleType anyRole = CompanyRoleTypeBuilder.aCompanyRoleType()
                .withCode("ANYTHNIG").build();
        CompanyRole companyRole = CompanyRoleBuilder.aCompanyRole()
                .withCompany(company).withId(companyRoleId).withRoleType(anyRole).build();
        CompanyEmployeeRelation relation = CompanyEmployeeRelationBuilder.aCompanyEmployeeRelation()
                .withCompanyRole(companyRole)
                .withValidDates(dateWindow).build();
        EmployeeCustomerRelation customerServiceRep = EmployeeCustomerRelationBuilder.anEmployeeCustomerRelation()
                .withCode("CUSTOMER_SERVICE_REP").withId(customerRelationId).build();


        CustomerServiceRepAuthorizedCompany authorizedCompany = CustomerServiceRepAuthorizedCompany.withCompanyEmployeeRelation(relation);

        when(companyRoleTypeRepository.findByCode("CUSTOMER")).thenReturn(customer);
        when(companyRoleRepository.findByCompanyId(companyId)).thenReturn(Arrays.asList(companyRole));
        when(employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP")).thenReturn(customerServiceRep);

        ArgumentCaptor<CompanyEmployeeRelation> argumentCaptor = ArgumentCaptor.forClass(CompanyEmployeeRelation.class);

        customerServiceRepController.saveCompanyRelation(employeeAccount, authorizedCompany);

        verify(companyEmployeeRelationRepository, times(0)).save(argumentCaptor.capture());
    }
}
