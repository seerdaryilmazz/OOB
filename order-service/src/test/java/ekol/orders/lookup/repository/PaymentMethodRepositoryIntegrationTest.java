package ekol.orders.lookup.repository;


import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.hibernate5.test.LookupRepositoryIntegrationTest;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.lookup.repository.PaymentMethodRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class PaymentMethodRepositoryIntegrationTest extends LookupRepositoryIntegrationTest<PaymentMethod> {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public Class<PaymentMethod> getType() {
        return PaymentMethod.class;
    }

    @Override
    public LookupRepository<PaymentMethod> getRepository() {
        return paymentMethodRepository;
    }
}