package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentVehicleRequirement;
import ekol.orders.order.domain.VehicleRequirementReason;

import java.util.List;

public interface OrderShipmentVehicleRequirementRepository extends ApplicationRepository<OrderShipmentVehicleRequirement> {

    List<OrderShipmentVehicleRequirement> findByShipmentAndRequirementReason(OrderShipment shipment, VehicleRequirementReason reason);
}
