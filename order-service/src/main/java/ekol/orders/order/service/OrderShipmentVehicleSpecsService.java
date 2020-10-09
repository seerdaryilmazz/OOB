package ekol.orders.order.service;

import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentVehicleRequirement;
import ekol.orders.order.domain.VehicleRequirementReason;
import ekol.orders.order.repository.OrderShipmentVehicleRequirementRepository;
import ekol.orders.order.validator.OrderShipmentVehicleValidator;
import ekol.orders.transportOrder.domain.OrderPlanningOperationType;
import ekol.orders.transportOrder.domain.VehicleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderShipmentVehicleSpecsService {

    private ListOrderShipmentService orderShipmentService;
    private OrderShipmentVehicleValidator orderShipmentVehicleValidator;
    private OrderShipmentVehicleRequirementRepository vehicleRequirementRepository;

    @Autowired
    public OrderShipmentVehicleSpecsService(ListOrderShipmentService orderShipmentService,
                                            OrderShipmentVehicleRequirementRepository vehicleRequirementRepository,
                                            OrderShipmentVehicleValidator orderShipmentVehicleValidator){
        this.orderShipmentService = orderShipmentService;
        this.vehicleRequirementRepository = vehicleRequirementRepository;
        this.orderShipmentVehicleValidator = orderShipmentVehicleValidator;
    }

    public void updateVehicleSpecsForOrder(Order order) {
        order.getShipments().forEach(this::updateVehicleSpecsForShipment);
    }

    public void updateVehicleSpecsForShipment(OrderShipment shipment) {
        updateCurtainSideSpecsIfNecessary(shipment);
        updateHangingLoadSpecsIfNecessary(shipment);
    }

    public void updateFrigoSpecsIfNecessary(OrderShipment shipment) {
        updateTemperatureControlledLoadsSpecsIfNecessary(shipment, OrderPlanningOperationType.COLLECTION);
        updateTemperatureControlledLoadsSpecsIfNecessary(shipment, OrderPlanningOperationType.DISTRIBUTION);
    }
    private void updateCurtainSideSpecsIfNecessary(OrderShipment shipment, OrderPlanningOperationType operationType){
        if(shipment.isLongLoad() && !shipment.hasVehicleRequirement(VehicleFeature.CURTAIN_SIDER, operationType, null)){
            shipment.getVehicleRequirements().add(
                    createVehicleRequirement(shipment, operationType, VehicleFeature.CURTAIN_SIDER));
        }else if(!shipment.isLongLoad()){
            shipment.deleteVehicleRequirements(VehicleFeature.CURTAIN_SIDER, operationType, VehicleRequirementReason.BY_LOAD);
        }
    }
    private void updateCurtainSideSpecsIfNecessary(OrderShipment shipment){
        updateCurtainSideSpecsIfNecessary(shipment, OrderPlanningOperationType.COLLECTION);
        updateCurtainSideSpecsIfNecessary(shipment, OrderPlanningOperationType.DISTRIBUTION);
    }

    private void updateHangingLoadSpecsIfNecessary(OrderShipment shipment, OrderPlanningOperationType operationType){
        if(shipment.isHangingLoad() && !shipment.hasVehicleRequirement(VehicleFeature.SUITABLE_FOR_HANGING_LOADS, operationType, null)){
            shipment.getVehicleRequirements().add(
                    createVehicleRequirement(shipment, operationType, VehicleFeature.SUITABLE_FOR_HANGING_LOADS));
        }else if(!shipment.isHangingLoad()){
            shipment.deleteVehicleRequirements(VehicleFeature.SUITABLE_FOR_HANGING_LOADS, operationType, VehicleRequirementReason.BY_LOAD);
        }
    }

    private void updateTemperatureControlledLoadsSpecsIfNecessary(OrderShipment shipment, OrderPlanningOperationType operationType){
        if(shipment.getTemperatureMinValue() != null || shipment.getTemperatureMaxValue() != null){
            if(!shipment.hasVehicleRequirement(VehicleFeature.FRIGO, operationType, VehicleRequirementReason.BY_LOAD)) {
                shipment.getVehicleRequirements().add(
                        createVehicleRequirement(shipment, operationType, VehicleFeature.FRIGO));
            }
            if(shipment.hasVehicleRequirement(VehicleFeature.FRIGO, operationType, VehicleRequirementReason.BY_ORDER)){
                shipment.deleteVehicleRequirements(VehicleFeature.FRIGO, operationType, VehicleRequirementReason.BY_ORDER);
            }
        }else if(shipment.getTemperatureMinValue() == null && shipment.getTemperatureMaxValue() == null){
            shipment.deleteVehicleRequirements(VehicleFeature.FRIGO, operationType, VehicleRequirementReason.BY_LOAD);
        }
    }

    private void updateHangingLoadSpecsIfNecessary(OrderShipment shipment){
        updateHangingLoadSpecsIfNecessary(shipment, OrderPlanningOperationType.COLLECTION);
        updateHangingLoadSpecsIfNecessary(shipment, OrderPlanningOperationType.DISTRIBUTION);
    }

    private OrderShipmentVehicleRequirement createVehicleRequirement(OrderShipment shipment, OrderPlanningOperationType operationType, VehicleFeature requirement){
        OrderShipmentVehicleRequirement vehicleRequirement = new OrderShipmentVehicleRequirement();
        vehicleRequirement.setOperationType(operationType);
        vehicleRequirement.setRequirement(requirement);
        vehicleRequirement.setRequirementReason(VehicleRequirementReason.BY_LOAD);
        vehicleRequirement.setShipment(shipment);
        return vehicleRequirement;
    }

    public OrderShipment updateVehicleRequirements(Long shipmentId, List<OrderShipmentVehicleRequirement> requirementsByOrder){
        requirementsByOrder.forEach(orderShipmentVehicleValidator::validate);
        OrderShipment shipment = orderShipmentService.getOrThrowException(shipmentId);
        List<OrderShipmentVehicleRequirement> existingRequirements =
                vehicleRequirementRepository.findByShipmentAndRequirementReason(shipment, VehicleRequirementReason.BY_ORDER);

        existingRequirements.forEach(existingRequirement -> {
            if(requirementsByOrder.stream().noneMatch(requirement -> requirement.getOperationType().equals(existingRequirement.getOperationType()) &&
                    requirement.getRequirement().equals(existingRequirement.getRequirement()))){
                existingRequirement.setDeleted(true);
                vehicleRequirementRepository.save(existingRequirement);
            }
        });

        requirementsByOrder.forEach(requirement -> {
            if(existingRequirements.stream().noneMatch(existingRequirement ->
                    existingRequirement.getOperationType().equals(requirement.getOperationType()) &&
                            existingRequirement.getRequirement().equals(requirement.getRequirement()))){
                requirement.setShipment(shipment);
                vehicleRequirementRepository.save(requirement);
            }
        });

        return shipment;
    }
}
