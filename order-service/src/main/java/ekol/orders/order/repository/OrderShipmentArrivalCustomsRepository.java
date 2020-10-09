package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;

import java.util.Optional;

public interface OrderShipmentArrivalCustomsRepository extends ApplicationRepository<OrderShipmentArrivalCustoms> {

    Optional<OrderShipmentArrivalCustoms> findByShipment(OrderShipment orderShipment);
}
