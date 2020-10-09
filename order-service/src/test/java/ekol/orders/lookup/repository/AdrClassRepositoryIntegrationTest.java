package ekol.orders.lookup.repository;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.hibernate5.test.LookupRepositoryIntegrationTest;
import ekol.orders.lookup.domain.AdrClass;
import ekol.orders.lookup.repository.AdrClassRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class AdrClassRepositoryIntegrationTest extends LookupRepositoryIntegrationTest<AdrClass> {

    @Autowired
    private AdrClassRepository adrClassRepository;

    @Override
    public Class<AdrClass> getType() {
        return AdrClass.class;
    }

    @Override
    public LookupRepository<AdrClass> getRepository() {
        return adrClassRepository;
    }
}
