package ekol.kartoteks.controller.api;

import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.builder.CompanyContactBuilder;
import ekol.kartoteks.controller.CompanyContactController;
import ekol.kartoteks.domain.CompanyContact;
import ekol.kartoteks.repository.CompanyContactRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by kilimci on 22/11/16.
 */
@RunWith(SpringRunner.class)
public class CompanyContactControllerUnitTest {

    @InjectMocks
    private CompanyContactController companyContactController;

    @Mock
    private CompanyContactRepository companyContactRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindContactById() {
        Long id = 1L;
        CompanyContact contact = CompanyContactBuilder.aCompanyContact().withId(id).withFirstName("firstname").withLastName("lastname").build();
        when(companyContactRepository.findOne(id)).thenReturn(contact);
        CompanyContact result = companyContactController.get(id);
        assertEquals(new Long(1), result.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenContactDoesNotExist() {
        Long id = 1L;
        when(companyContactRepository.findOne(id)).thenReturn(null);
        companyContactController.get(id);
    }

}
