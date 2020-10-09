package ekol.orders.lookup.repository;


import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.hibernate5.test.LookupRepositoryIntegrationTest;
import ekol.orders.lookup.domain.PackageType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class PackageTypeRepositoryIntegrationTest extends LookupRepositoryIntegrationTest<PackageType> {

    @Autowired
    private PackageTypeRepository packageTypeRepository;

    @Override
    public Class<PackageType> getType() {
        return PackageType.class;
    }

    @Override
    public LookupRepository<PackageType> getRepository() {
        return packageTypeRepository;
    }
}
