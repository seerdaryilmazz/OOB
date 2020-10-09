package ekol.location.other;

import ekol.hibernate5.test.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ozer on 01/02/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class BuilderClassAndEntityClassCompatibilityIntegrationTest {

    @Test
    public void builderClassesAndEntityClassesMustBeCompatible() {
        TestUtils.checkBuilderClassAndEntityClassCompatibility();
    }
}
