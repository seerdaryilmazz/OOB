package ekol.crm.account.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class PotentialRepositoryIntegrationTest {


    @Autowired
    private PotentialRepository repository;

    @Test
    public void shouldSave() {

    }

//    private List<Long> cleanupIds = new ArrayList<>();
//
//    @Before
//    public void init(){
//        cleanup();
//    }
//
//    @After
//    public void cleanup(){
//        cleanupIds.forEach(id -> {
//            Potential entity = repository.findOne(id);
//            entity.setDeleted(true);
//            repository.save(entity);
//        });
//        cleanupIds = new ArrayList<>();
//    }
//
//    @Test
//    public void shouldSavePotential() {
//
//        Potential potential = MockData.ekolSpainPotential.build();
//        potential.setAccount(MockData.ekolSpainAccount.but().withId(-1L).build());
//
//        Potential result = this.repository.save(potential);
//        assertNotNull(result.getId());
//        assertEquals(result.getAccount().getId(), potential.getAccount().getId());
//        assertEquals(result.getFromCountry().getIso(), potential.getFromCountry().getIso());
//        assertEquals(result.getToCountry().getIso(), potential.getToCountry().getIso());
//        assertEquals(result.getShipmentLoadingType(), potential.getShipmentLoadingType());
//        assertEquals(result.getFrequencyType(), potential.getFrequencyType());
//        assertEquals(result.getFrequency(), potential.getFrequency());
//        cleanupIds.add(result.getId());
//    }
//
//    @Test
//    public void shouldFindById(){
//        Optional<Potential> result = this.repository.findById(-1L);
//        System.out.println(result);
//        assertTrue(result.isPresent());
//
//        Optional<Potential> notFound = this.repository.findById(100L);
//        assertFalse(notFound.isPresent());
//    }
//
//    @Test
//    public void shouldFindByAccountId() {
//        List<Potential> result = this.repository.findByAccountIdAndStatus(-1L, PotentialStatus.ACTIVE);
//        System.out.println(result);
//        assertFalse(result.isEmpty());
//
//        List<Potential> notFound = this.repository.findByAccountIdAndStatus(100L, PotentialStatus.ACTIVE);
//        assertTrue(notFound.isEmpty());
//    }


}
