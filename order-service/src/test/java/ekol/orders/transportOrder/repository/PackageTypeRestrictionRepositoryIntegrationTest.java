package ekol.orders.transportOrder.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.repository.PackageTypeRepository;
import ekol.orders.testdata.SomeData;
import ekol.orders.transportOrder.domain.PackageTypeRestriction;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class PackageTypeRestrictionRepositoryIntegrationTest {

    @Autowired
    private PackageTypeRepository packageTypeRepository;

    @Autowired
    private PackageTypeRestrictionRepository packageTypeRestrictionRepository;

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(
                packageTypeRestrictionRepository,
                packageTypeRepository);
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

        PackageType persistedPackageType = packageTypeRepository.save(SomeData.somePackageType().build());

        PackageTypeRestriction persistedEntity = packageTypeRestrictionRepository.save(SomeData.somePackageTypeRestriction(persistedPackageType).build());

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }

    @Test
    public void findByPackageTypeIdAndDeletedIsFalseMustWorkCorrectly() {

        PackageType persistedPackageType = packageTypeRepository.save(SomeData.somePackageType().build());

        PackageTypeRestriction persistedEntity = packageTypeRestrictionRepository.save(SomeData.somePackageTypeRestriction(persistedPackageType).build());

        PackageTypeRestriction queriedEntity = packageTypeRestrictionRepository.findByPackageTypeIdAndDeletedIsFalse(persistedPackageType.getId());

        Assert.assertNotNull("'find by package type id' does not work correctly.", queriedEntity);
        Assert.assertNotNull("'find by package type id' does not work correctly.", queriedEntity.getId());
        Assert.assertTrue("'find by package type id' does not work correctly.", queriedEntity.getId().equals(persistedEntity.getId()));
        Assert.assertNotNull("'find by package type id' does not work correctly.", queriedEntity.getPackageType());
    }

    /**
     * Burada asıl test etmek istediğimiz listedeki entity'lerin packageType alanlarının dolu gelip gelmediğidir.
     */
    @Test
    public void findAllByDeletedIsFalseMustWorkCorrectly() {

        PackageType persistedPackageType = packageTypeRepository.save(SomeData.somePackageType().build());

        PackageTypeRestriction persistedEntity = packageTypeRestrictionRepository.save(SomeData.somePackageTypeRestriction(persistedPackageType).build());

        List<PackageTypeRestriction> list = packageTypeRestrictionRepository.findAllByDeletedIsFalse();

        Assert.assertNotNull("'find all with package type' does not work correctly.", list);
        Assert.assertTrue("'find all with package type' does not work correctly.", list.size() == 1);
        Assert.assertNotNull("'find all with package type' does not work correctly.", list.get(0).getPackageType());
    }
}
