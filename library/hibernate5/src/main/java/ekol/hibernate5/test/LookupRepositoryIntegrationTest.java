package ekol.hibernate5.test;

import ekol.exceptions.ApplicationException;
import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.hibernate5.domain.repository.LookupRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * LookupEntity'den türeyen sınıfların repository entegrasyon testleri için genel test sınıfı
 */
public abstract class LookupRepositoryIntegrationTest<T extends LookupEntity> {

    public abstract Class<T> getType();

    public abstract LookupRepository<T> getRepository();

    public T createEmptyInstance() {

        try {
            return getType().newInstance();
        } catch (InstantiationException e) {
            throw new ApplicationException("", e);
        } catch (IllegalAccessException e) {
            throw new ApplicationException("", e);
        }
    }

    public T save(String code, String name, boolean deleted) {

        T newEntity = createEmptyInstance();
        newEntity.setCode(code);
        newEntity.setName(name);
        newEntity.setDeleted(deleted);

        return getRepository().save(newEntity);
    }

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(getRepository());
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

        T persistedEntity = save("AAA", "ZZZ", false);

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }

    @Test
    public void findByCodeMustWorkCorrectly() {

        T persistedEntity = save("AAA", "ZZZ", false);

        T persistedEntityByCode = getRepository().findByCode("AAA");

        Assert.assertNotNull("'find by code' does not work correctly.", persistedEntityByCode);
        Assert.assertTrue("'find by code' does not work correctly.", persistedEntityByCode.getId().equals(persistedEntity.getId()));

        T persistedEntityByAnotherCode = getRepository().findByCode("BBB");

        Assert.assertNull("'find by code' does not work correctly.", persistedEntityByAnotherCode);
    }

    @Test
    public void findAllOrderByCodeMustWorkCorrectly() {

        save("BBB", "ZZZ", false);
        save("AAA", "YYY", false);
        save("CCC", "XXX", false);

        List<T> list = getRepository().findByOrderByCode();

        Assert.assertNotNull("'find all order by code' does not work correctly.", list);
        Assert.assertTrue("'find all order by code' does not work correctly.", list.size() == 3);
        Assert.assertTrue("'find all order by code' does not work correctly.", "AAA".equals(list.get(0).getCode()));
        Assert.assertTrue("'find all order by code' does not work correctly.", "BBB".equals(list.get(1).getCode()));
        Assert.assertTrue("'find all order by code' does not work correctly.", "CCC".equals(list.get(2).getCode()));
    }

    @Test
    public void findAllOrderByNameMustWorkCorrectly() {

        save("BBB", "ZZZ", false);
        save("AAA", "YYY", false);
        save("CCC", "XXX", false);

        List<T> list = getRepository().findByOrderByName();

        Assert.assertNotNull("'find all order by name' does not work correctly.", list);
        Assert.assertTrue("'find all order by name' does not work correctly.", list.size() == 3);
        Assert.assertTrue("'find all order by name' does not work correctly.", "XXX".equals(list.get(0).getName()));
        Assert.assertTrue("'find all order by name' does not work correctly.", "YYY".equals(list.get(1).getName()));
        Assert.assertTrue("'find all order by name' does not work correctly.", "ZZZ".equals(list.get(2).getName()));
    }

}
