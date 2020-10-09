package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipmentEquipmentRequirement;

import java.util.Optional;

public interface OrderShipmentEquipmentRequirementRepository extends ApplicationRepository<OrderShipmentEquipmentRequirement> {

    Optional<OrderShipmentEquipmentRequirement> findById(Long id);
}
