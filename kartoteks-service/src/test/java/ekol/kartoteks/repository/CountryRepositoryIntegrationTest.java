package ekol.kartoteks.repository;

import ekol.kartoteks.builder.CountryBuilder;
import ekol.kartoteks.domain.common.Country;
import ekol.kartoteks.repository.common.CountryRepository;
import ekol.kartoteks.testdata.SomeData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kilimci on 14/10/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class CountryRepositoryIntegrationTest {

    @Autowired
    private CountryRepository countryRepository;

    private static CountryBuilder someCountry = SomeData.someCountry();

    private List<Long> cleanupIds = new ArrayList<>();

    @Before
    public void init(){
        cleanup();
    }
    @After
    public void cleanup(){
        cleanupIds.forEach(id -> {
            Country c = countryRepository.findOne(id);
            c.setDeleted(true);
            countryRepository.save(c);
        });
        cleanupIds.clear();
    }

    @Test
    public void shouldSaveCountry(){
        Country saved = countryRepository.save(someCountry.build());
        assertNotNull(saved.getId());
        cleanupIds.add(saved.getId());
    }

    @Test
    public void shouldFindAllByOrderByCountryName(){
        List<Country> countries = countryRepository.findAllByOrderByCountryName();
        assertThat(countries.size(), is(3));
        assertThat(countries.get(0).getCountryName(), is("AAAAA"));
        assertThat(countries.get(1).getCountryName(), is("GGGGG"));
        assertThat(countries.get(2).getCountryName(), is("ZZZZZ"));
    }

}
