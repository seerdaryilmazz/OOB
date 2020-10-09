package ekol.orders.lookup.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.lookup.domain.HSCode;
import ekol.orders.lookup.repository.HSCodeRepository;
import ekol.orders.testdata.SomeData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * HSCode aslında LookupEntity'den türüyor ama diğer lookup sınıflarından farklı olarak
 * 'tier' diye bir alanı daha var. Bu nedenle bu entegrasyon testi diğer lookup sınıflarından farklı olarak
 * LookupRepositoryIntegrationTest'ten türemiyor.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class HSCodeRepositoryIntegrationTest {

    @Autowired
    private HSCodeRepository hsCodeRepository;

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(hsCodeRepository);
    }

    @Before
    public void runBeforeEveryTest() {
        cleanup();
    }

    @After
    public void runAfterEveryTest() {
        cleanup();
    }

    @Test
    public void saveMustWorkCorrectly() {

        HSCode persistedEntity = hsCodeRepository.save(SomeData.someHSCode().withCode("AAA").withName("ZZZ").build());

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }

    @Test
    public void findByCodeMustWorkCorrectly() {

        HSCode persistedEntity = hsCodeRepository.save(SomeData.someHSCode().withCode("AAA").withName("ZZZ").build());

        HSCode persistedEntityByCode = hsCodeRepository.findByCode("AAA");

        Assert.assertNotNull("'find by code' does not work correctly.", persistedEntityByCode);
        Assert.assertTrue("'find by code' does not work correctly.", persistedEntityByCode.getId().equals(persistedEntity.getId()));

        HSCode persistedEntityByAnotherCode = hsCodeRepository.findByCode("BBB");

        Assert.assertNull("'find by code' does not work correctly.", persistedEntityByAnotherCode);
    }

    @Test
    public void findAllOrderByCodeMustWorkCorrectly() {

        hsCodeRepository.save(SomeData.someHSCode().withCode("BBB").withName("ZZZ").build());
        hsCodeRepository.save(SomeData.someHSCode().withCode("AAA").withName("YYY").build());
        hsCodeRepository.save(SomeData.someHSCode().withCode("CCC").withName("XXX").build());

        List<HSCode> list = hsCodeRepository.findByOrderByCode();

        Assert.assertTrue("'count' does not work correctly.", hsCodeRepository.count() == 3);
        Assert.assertTrue("'find all order by code' does not work correctly.", "AAA".equals(list.get(0).getCode()));
        Assert.assertTrue("'find all order by code' does not work correctly.", "BBB".equals(list.get(1).getCode()));
        Assert.assertTrue("'find all order by code' does not work correctly.", "CCC".equals(list.get(2).getCode()));
    }

    @Test
    public void findAllOrderByNameMustWorkCorrectly() {

        hsCodeRepository.save(SomeData.someHSCode().withCode("BBB").withName("ZZZ").build());
        hsCodeRepository.save(SomeData.someHSCode().withCode("AAA").withName("YYY").build());
        hsCodeRepository.save(SomeData.someHSCode().withCode("CCC").withName("XXX").build());

        List<HSCode> list = hsCodeRepository.findByOrderByName();

        Assert.assertTrue("'count' does not work correctly.", hsCodeRepository.count() == 3);
        Assert.assertTrue("'find all order by name' does not work correctly.", "XXX".equals(list.get(0).getName()));
        Assert.assertTrue("'find all order by name' does not work correctly.", "YYY".equals(list.get(1).getName()));
        Assert.assertTrue("'find all order by name' does not work correctly.", "ZZZ".equals(list.get(2).getName()));
    }

}
