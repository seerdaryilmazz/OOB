package ekol.kartoteks.repository;

import ekol.kartoteks.testdata.SomePersistedData;
import ekol.kartoteks.domain.CompanyRelation;
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

import static ekol.kartoteks.testdata.SomeData.someCompanyRelation;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kilimci on 17/10/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class CompanyRelationRepositoryIntegrationTest {

    @Autowired
    private CompanyRelationRepository repository;

    @Autowired
    private SomePersistedData persistedData;

    private List<Long> cleanupIds = new ArrayList<>();

    @Before
    public void init(){
        cleanup();
    }
    @After
    public void cleanup(){
        cleanupIds.forEach(id -> {
            CompanyRelation entity = repository.findOne(id);
            entity.setDeleted(true);
            repository.save(entity);
        });
        cleanupIds.clear();
    }

    @Test
    public void shouldSaveCompanyRelation(){
        CompanyRelation relation = someCompanyRelation().but().withActiveCompany(persistedData.someCompany(-1))
                .withPassiveCompany(persistedData.someCompany(-2))
                .withRelationType(persistedData.someCompanyRelationType()).build();
        CompanyRelation entity = repository.save(relation);
        assertNotNull(entity.getId());
        cleanupIds.add(entity.getId());
    }
}
