package ekol.orders.order.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.order.builder.MockOrderData;
import ekol.orders.order.domain.Order;
import ekol.orders.order.service.CodeGenerator;
import ekol.orders.order.service.RedisAtomicIncrementer;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class OrderRepositoryIntegrationTest {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private CodeGenerator codeGenerator;

    @After
    public void cleanup(){
        TestUtils.softDeleteAll(repository);
    }

    @Test
    public void givenValidOrder_whenSave_thenSaveTheOrder() {
        Order order = MockOrderData.validNewOrder().withCode(codeGenerator.getNewOrderCode()).build();
        Order response = this.repository.save(order);

        assertNotNull(response.getId());
    }
}
