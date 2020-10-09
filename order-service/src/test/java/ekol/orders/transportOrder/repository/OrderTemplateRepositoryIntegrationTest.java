package ekol.orders.transportOrder.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.lookup.repository.IncotermRepository;
import ekol.orders.lookup.repository.PaymentMethodRepository;
import ekol.orders.testdata.SomeData;
import ekol.orders.transportOrder.domain.OrderTemplate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class OrderTemplateRepositoryIntegrationTest {

    @Autowired
    private IncotermRepository incotermRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private OrderTemplateRepository orderTemplateRepository;

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(
                orderTemplateRepository,
                paymentMethodRepository,
                incotermRepository);
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

        Set<Incoterm> incoterms = new HashSet<>();
        incoterms.add(incotermRepository.save(SomeData.someIncoterm().withCode("AAA").build()));
        incoterms.add(incotermRepository.save(SomeData.someIncoterm().withCode("BBB").build()));

        Set<PaymentMethod> paymentMethods = new HashSet<>();
        paymentMethods.add(paymentMethodRepository.save(SomeData.somePaymentMethod().withCode("XXX").build()));
        paymentMethods.add(paymentMethodRepository.save(SomeData.somePaymentMethod().withCode("YYY").build()));

        OrderTemplate persistedEntity = orderTemplateRepository.save(SomeData.someOrderTemplate(incoterms, paymentMethods).build());

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }
}
