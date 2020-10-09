package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipmentUnit;

import java.util.Optional;

public interface OrderShipmentUnitRepository extends ApplicationRepository<OrderShipmentUnit> {

    Optional<OrderShipmentUnit> findById(Long id);
}
