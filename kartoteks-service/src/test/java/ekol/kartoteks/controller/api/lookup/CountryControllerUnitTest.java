package ekol.kartoteks.controller.api.lookup;

import ekol.kartoteks.builder.CountryBuilder;
import ekol.kartoteks.controller.CountryController;
import ekol.kartoteks.domain.common.Country;
import ekol.kartoteks.repository.common.CountryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by kilimci on 22/11/16.
 */
@RunWith(SpringRunner.class)
public class CountryControllerUnitTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryController countryController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindAll() {
        Long id = 1L;
        Country country = CountryBuilder.aCountry().withCountryName("name").withId(id).build();
        when(countryRepository.findAllByOrderByCountryName()).thenReturn(Arrays.asList(country));
        List<Country> result = countryController.findAll();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    public void shouldDoSarch() {
        Long id = 1L;
        String q = "a";
        Country country = CountryBuilder.aCountry().withCountryName("name").withId(id).build();
        when(countryRepository.findByIsoStartingWithIgnoreCase(q)).thenReturn(Arrays.asList(country));
        List<Country> result = countryController.search(q);

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }
}
