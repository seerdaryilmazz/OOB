package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface OrderRepository extends ApplicationRepository<Order> {

    Optional<Order> findById(Long id);

    @EntityGraph(value = "Order.withShipments", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Order> findWithShipmentsById(Long id);

    @EntityGraph(value = "Order.withShipments", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Order> findWithShipmentsByCode(String code);

    @EntityGraph(value = "Order.withDocuments", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Order> findWithDocumentsById(Long id);
}
