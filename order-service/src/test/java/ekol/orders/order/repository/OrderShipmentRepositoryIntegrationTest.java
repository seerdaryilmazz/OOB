package ekol.orders.order.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.order.builder.MockOrderData;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;
import ekol.orders.order.service.CodeGenerator;
import ekol.orders.order.service.RedisAtomicIncrementer;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.orders.order.builder.MockOrderData.customsArrivalTR;
import static ekol.orders.order.builder.MockOrderData.customsDepartureEU;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class OrderShipmentRepositoryIntegrationTest {

    @Autowired
    private OrderShipmentRepository shipmentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CodeGenerator codeGenerator;

    @After
    public void cleanup(){
        TestUtils.softDeleteAll(orderRepository, shipmentRepository);
    }

    @Test
    public void givenValidShipment_whenSave_thenSaveTheShipment() {
        Order order = MockOrderData.validNewOrder().withCode(codeGenerator.getNewOrderCode()).build();
        Order saved = orderRepository.save(order);

        OrderShipment shipment = MockOrderData.newShipment1()
                .withOrder(saved)
                .withArrivalCustoms(null)
                .withDepartureCustoms(null)
                .withCode(codeGenerator.getNewShipmentCode())
                .build();
        OrderShipment response = this.shipmentRepository.save(shipment);

        assertNotNull(response.getId());
    }
}
