package ekol.location.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.location.domain.Zone;
import ekol.location.domain.ZoneType;
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
public class ZoneRepositoryIntegrationTest {

    @Autowired
    private ZoneTypeRepository zoneTypeRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    public void cleanup() {
        TestUtils.softDeleteAll(zoneRepository, zoneTypeRepository);
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

        Assert.assertNotNull("'save' does not work correctly.", zone);
        Assert.assertNotNull("'save' does not work correctly.", zone.getId());
    }
}