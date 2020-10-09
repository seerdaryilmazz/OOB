package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipmentAdr;

import java.util.Optional;

public interface OrderShipmentAdrRepository extends ApplicationRepository<OrderShipmentAdr> {

    Optional<OrderShipmentAdr> findById(Long id);
}
