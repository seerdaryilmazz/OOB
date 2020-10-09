package ekol.orders.transportOrder.repository;


import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.hibernate5.test.LookupRepositoryIntegrationTest;
import ekol.orders.transportOrder.domain.TransportType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class TransportTypeRepositoryIntegrationTest extends LookupRepositoryIntegrationTest<TransportType> {

    @Autowired
    private TransportTypeRepository transportTypeRepository;

    @Override
    public Class<TransportType> getType() {
        return TransportType.class;
    }

    @Override
    public LookupRepository<TransportType> getRepository() {
        return transportTypeRepository;
    }
}
