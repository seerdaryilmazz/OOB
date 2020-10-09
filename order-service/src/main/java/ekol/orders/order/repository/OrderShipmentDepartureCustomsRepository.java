package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;

import java.util.Optional;

public interface OrderShipmentDepartureCustomsRepository extends ApplicationRepository<OrderShipmentDepartureCustoms> {

    Optional<OrderShipmentDepartureCustoms> findByShipment(OrderShipment orderShipment);
}
