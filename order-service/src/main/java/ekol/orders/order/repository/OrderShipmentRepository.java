package ekol.orders.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipment;

public interface OrderShipmentRepository extends ApplicationRepository<OrderShipment> {

	Optional<OrderShipment> findById(Long id);
	
    Optional<OrderShipment> findByCode(String code);

    @EntityGraph(value = "OrderShipment.withOrder", type = EntityGraph.EntityGraphType.LOAD)
    List<OrderShipment> findWithOrderByOrderId(Long id);

    @EntityGraph(value = "OrderShipment.withOrder", type = EntityGraph.EntityGraphType.LOAD)
    List<OrderShipment> findWithOrderByOrderCode(String code);

    @EntityGraph(value = "OrderShipment.withOrder", type = EntityGraph.EntityGraphType.LOAD)
    Optional<OrderShipment> findWithOrderByCode(String code);

    @EntityGraph(value = "OrderShipment.withUnits", type = EntityGraph.EntityGraphType.LOAD)
    Optional<OrderShipment> findWithUnitsById(Long id);

    @EntityGraph(value = "OrderShipment.withOrder", type = EntityGraph.EntityGraphType.LOAD)
    Optional<OrderShipment> findWithOrderById(Long id);


    @EntityGraph(value = "OrderShipment.withOrderNumbers", type = EntityGraph.EntityGraphType.LOAD)
    Optional<OrderShipment> findWithOrderNumbersById(Long id);

    @EntityGraph(value = "OrderShipment.withDocuments", type = EntityGraph.EntityGraphType.LOAD)
    Optional<OrderShipment> findWithDocumentsById(Long id);
}
