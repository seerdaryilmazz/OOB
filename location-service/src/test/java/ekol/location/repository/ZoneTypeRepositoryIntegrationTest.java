package ekol.location.repository;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.hibernate5.test.LookupRepositoryIntegrationTest;
import ekol.location.domain.ZoneType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class ZoneTypeRepositoryIntegrationTest extends LookupRepositoryIntegrationTest<ZoneType> {

    @Autowired
    private ZoneTypeRepository zoneTypeRepository;

    @Override
    public Class<ZoneType> getType() {
        return ZoneType.class;
    }

    @Override
    public LookupRepository<ZoneType> getRepository() {
        return zoneTypeRepository;
    }
}
