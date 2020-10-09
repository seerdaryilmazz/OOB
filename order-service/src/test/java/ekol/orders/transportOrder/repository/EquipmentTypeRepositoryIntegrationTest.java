package ekol.orders.transportOrder.repository;


import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.hibernate5.test.LookupRepositoryIntegrationTest;
import ekol.orders.transportOrder.domain.EquipmentType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class EquipmentTypeRepositoryIntegrationTest extends LookupRepositoryIntegrationTest<EquipmentType> {

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;

    @Override
    public Class<EquipmentType> getType() {
        return EquipmentType.class;
    }

    @Override
    public LookupRepository<EquipmentType> getRepository() {
        return equipmentTypeRepository;
    }
}
