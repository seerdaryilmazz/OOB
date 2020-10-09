package ekol.kartoteks.controller.api;

import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.builder.CompanyBuilder;
import ekol.kartoteks.builder.CompanyContactBuilder;
import ekol.kartoteks.builder.CompanyLocationBuilder;
import ekol.kartoteks.controller.CompanyController;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyContact;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.repository.CompanyContactRepository;
import ekol.kartoteks.repository.CompanyCustomRepository;
import ekol.kartoteks.repository.CompanyLocationRepository;
import ekol.kartoteks.repository.CompanyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by kilimci on 22/11/16.
 */
@RunWith(SpringRunner.class)
public class CompanyControllerUnitTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyCustomRepository companyCustomRepository;

    @Mock
    private CompanyLocationRepository companyLocationRepository;

    @Mock
    private CompanyContactRepository companyContactRepository;

    @InjectMocks
    private CompanyController companyController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldFindCompanyById() {
        Long id = 1L;
        Company company = CompanyBuilder.aCompany().withId(id).withName("name").build();
        when(companyCustomRepository.findById(id)).thenReturn(company);
        Company result = companyController.get(id);
        assertEquals(new Long(1), result.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenCompanyDoesNotExist() {
        Long id = 1L;
        when(companyCustomRepository.findById(id)).thenReturn(null);
        companyController.get(id);
    }

    @Test
    public void shouldFindLocationsByCompanyId() {
        Long id = 1L;
        Company company = CompanyBuilder.aCompany().withId(id).withName("name").build();
        CompanyLocation location = CompanyLocationBuilder.aCompanyLocation().withCompany(company).withId(id).withName("location").build();

        when(companyRepository.findById(id)).thenReturn(company);
        when(companyLocationRepository.findByCompanyId(id)).thenReturn(Stream.of(location).collect(Collectors.toSet()));
        Set<CompanyLocation> result = companyController.getLocations(id);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldFindContactsByCompanyId() {
        Long id = 1L;
        Company company = CompanyBuilder.aCompany().withId(id).withName("name").build();
        CompanyContact contact = CompanyContactBuilder.aCompanyContact().withCompany(company).withId(id)
                .withFirstName("firstname").withLastName("lastname").build();

        when(companyRepository.findById(id)).thenReturn(company);
        when(companyContactRepository.findByCompanyId(id)).thenReturn(Stream.of(contact).collect(Collectors.toSet()));
        Set<CompanyContact> result = companyController.getContacts(id);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldFindEkolOwnedCompanies() {
        Long id = 1L;
        Company company = CompanyBuilder.aCompany().withId(id).withName("ekol").withOwnedByEkol(true).build();
        when(companyRepository.findByOwnedByEkol(true)).thenReturn(Arrays.asList(company));
        List<Company> result =  companyController.getEkolOwnedCompanies();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }
}
