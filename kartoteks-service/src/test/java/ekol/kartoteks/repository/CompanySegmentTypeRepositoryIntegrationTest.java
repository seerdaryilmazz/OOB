package ekol.kartoteks.repository;

import ekol.kartoteks.domain.CompanySegmentType;
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

import static ekol.kartoteks.testdata.SomeData.someCompanySegmentType;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kilimci on 17/10/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class CompanySegmentTypeRepositoryIntegrationTest {

    @Autowired
    private CompanySegmentTypeRepository repository;

    private List<Long> cleanupIds = new ArrayList<>();

    @Before
    public void init(){
        cleanup();
    }
    @After
    public void cleanup(){
        cleanupIds.forEach(id -> {
            CompanySegmentType entity = repository.findOne(id);
            entity.setDeleted(true);
            repository.save(entity);
        });
        cleanupIds.clear();
    }

    @Test
    public void shouldSaveCompanySegmentType(){
        CompanySegmentType entity = repository.save(someCompanySegmentType().build());
        assertNotNull(entity.getId());
        cleanupIds.add(entity.getId());
    }
}
