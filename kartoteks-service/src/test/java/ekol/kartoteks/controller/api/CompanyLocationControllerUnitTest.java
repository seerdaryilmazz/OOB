package ekol.kartoteks.controller.api;

import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.builder.CompanyLocationBuilder;
import ekol.kartoteks.controller.CompanyLocationController;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.repository.CompanyLocationRepository;
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
public class CompanyLocationControllerUnitTest {

    @InjectMocks
    private CompanyLocationController companyLocationController;

    @Mock
    private CompanyLocationRepository companyLocationRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindLocationById() {
        Long id = 1L;
        CompanyLocation location = CompanyLocationBuilder.aCompanyLocation().withId(id).withName("location").build();
        when(companyLocationRepository.findOne(id)).thenReturn(location);
        CompanyLocation result = companyLocationController.getById(id);
        assertEquals(new Long(1), result.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundWhenLocationDoesNotExist() {
        Long id = 1L;
        when(companyLocationRepository.findOne(id)).thenReturn(null);
        companyLocationController.getById(id);
    }
}
