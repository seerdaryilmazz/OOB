package ekol.location.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.location.domain.PolygonRegion;
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
public class PolygonRegionRepositoryIntegrationTest {

    @Autowired
    private PolygonRegionRepository polygonRegionRepository;

    public void cleanup() {
        TestUtils.softDeleteAll(polygonRegionRepository);
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
        PolygonRegion polygonRegion = polygonRegionRepository.save(SomeData.somePolygonRegion());

        Assert.assertNotNull("'save' does not work correctly.", polygonRegion);
        Assert.assertNotNull("'save' does not work correctly.", polygonRegion.getId());
    }
}