package ekol.crm.account.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;



@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository repository;

    private List<Long> cleanupIds = new ArrayList<>();

    @Test
    public void shouldSave() {

    }

//    @Before
//    public void init(){
//        cleanup();
//    }
//
//    @After
//    public void cleanup(){
//        cleanupIds.forEach(id -> {
//            Account entity = repository.findOne(id);
//            entity.setDeleted(true);
//            repository.save(entity);
//        });
//        cleanupIds = new ArrayList<>();
//    }
//
//    @Test
//    public void shouldSaveAccount() {
//
//        Account account = MockData.ekolSpainAccount.build();
//        Account result = this.repository.save(MockData.ekolSpainAccount.build());
//        assertNotNull(result.getId());
//        assertEquals(result.getName(), account.getName());
//        assertEquals(result.getCountry().getIso(), account.getCountry().getIso());
//        assertEquals(result.getAccountType(), account.getAccountType());
//        assertEquals(result.getCompany().getId(), account.getCompany().getId());
//        cleanupIds.add(result.getId());
//    }
//
//    @Test
//    public void shouldFindById(){
//        Optional<Account> result = this.repository.findById(-1L);
//        assertTrue(result.isPresent());
//
//        Optional<Account> notFound = this.repository.findById(100L);
//        assertFalse(notFound.isPresent());
//    }
//
//    @Test
//    public void shouldFindByCompanyId() {
//        Optional<Account> result = this.repository.findByCompanyId(-1L);
//        assertTrue(result.isPresent());
//
//        Optional<Account> notFound = this.repository.findByCompanyId(100L);
//        assertFalse(notFound.isPresent());
//    }


}
