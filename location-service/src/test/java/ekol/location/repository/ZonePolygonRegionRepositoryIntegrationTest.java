package ekol.location.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.location.domain.*;
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
public class ZonePolygonRegionRepositoryIntegrationTest {

    @Autowired
    private ZoneTypeRepository zoneTypeRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ZoneZipCodeRepository zoneZipCodeRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PolygonRegionRepository polygonRegionRepository;

    @Autowired
    private ZonePolygonRegionRepository zonePolygonRegionRepository;

    public void cleanup() {
        TestUtils.softDeleteAll(zoneRepository, zoneTypeRepository, zoneZipCodeRepository, countryRepository, zonePolygonRegionRepository, polygonRegionRepository);
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
        ZoneType zoneType = zoneTypeRepository.save(SomeData.someZoneType());
        Zone zone = zoneRepository.save(SomeData.someZone(zoneType, null, null, null));
        Country country = countryRepository.save(SomeData.someCountry());
        PolygonRegion polygonRegion = polygonRegionRepository.save(SomeData.somePolygonRegion());
        ZoneZipCode zoneZipCode = zoneZipCodeRepository.save(SomeData.someZoneZipCode(zone, country));

        ZonePolygonRegion zonePolygonRegion = zonePolygonRegionRepository.save(SomeData.someZonePolygonRegion(zone, polygonRegion, zoneZipCode));

        Assert.assertNotNull("'save' does not work correctly.", zonePolygonRegion);
        Assert.assertNotNull("'save' does not work correctly.", zonePolygonRegion.getId());
    }
}