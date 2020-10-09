package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipmentDocument;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface OrderShipmentDocumentRepository extends ApplicationRepository<OrderShipmentDocument> {

    List<OrderShipmentDocument> findByShipmentId(Long shipmentId);

    Optional<OrderShipmentDocument> findById(Long id);

    @EntityGraph(value = "OrderShipmentDocument.withShipment", type = EntityGraph.EntityGraphType.LOAD)
    Optional<OrderShipmentDocument> findWithShipmentById(Long id);
}
