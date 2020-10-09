package ekol.location.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.location.domain.City;
import ekol.location.domain.Country;
import ekol.location.testdata.SomeData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ozer on 31/01/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class CityRepositoryIntegrationTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    public void cleanup() {
        TestUtils.softDeleteAll(cityRepository, countryRepository);
    }

    @Before
    public void runBeforeEveryTest() {
        cleanup();
    }

    @After
    public void runAfterEveryTest() {
        cleanup();
    }

    @Test
    public void saveMustWorkCorrectly() {
        Country country = countryRepository.save(SomeData.someCountry());
        City city = cityRepository.save(SomeData.someCity(country));

        Assert.assertNotNull("'save' does not work correctly.", city);
        Assert.assertNotNull("'save' does not work correctly.", city.getId());
    }
}