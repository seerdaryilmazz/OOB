package ekol.orders.order.builder;


import ekol.orders.order.domain.VehicleRequirementReason;
import ekol.orders.order.domain.dto.json.OrderShipmentVehicleRequirementJson;
import ekol.orders.transportOrder.domain.OrderPlanningOperationType;
import ekol.orders.transportOrder.domain.VehicleFeature;

public final class OrderShipmentVehicleRequirementJsonBuilder {
    private Long id;
    private VehicleFeature requirement;
    private OrderPlanningOperationType operationType;
    private VehicleRequirementReason requirementReason;

    private OrderShipmentVehicleRequirementJsonBuilder() {
    }

    public static OrderShipmentVehicleRequirementJsonBuilder anOrderShipmentVehicleRequirementJson() {
        return new OrderShipmentVehicleRequirementJsonBuilder();
    }

    public OrderShipmentVehicleRequirementJsonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentVehicleRequirementJsonBuilder withRequirement(VehicleFeature requirement) {
        this.requirement = requirement;
        return this;
    }

    public OrderShipmentVehicleRequirementJsonBuilder withOperationType(OrderPlanningOperationType operationType) {
        this.operationType = operationType;
        return this;
    }
    public OrderShipmentVehicleRequirementJsonBuilder withRequirementReason(VehicleRequirementReason requirementReason) {
        this.requirementReason = requirementReason;
        return this;
    }

    public OrderShipmentVehicleRequirementJsonBuilder but() {
        return anOrderShipmentVehicleRequirementJson().withId(id).withRequirement(requirement).withOperationType(operationType)
                .withRequirementReason(requirementReason);
    }

    public OrderShipmentVehicleRequirementJson build() {
        OrderShipmentVehicleRequirementJson orderShipmentVehicleRequirementJson = new OrderShipmentVehicleRequirementJson();
        orderShipmentVehicleRequirementJson.setId(id);
        orderShipmentVehicleRequirementJson.setRequirement(requirement);
        orderShipmentVehicleRequirementJson.setOperationType(operationType);
        orderShipmentVehicleRequirementJson.setRequirementReason(requirementReason);
        return orderShipmentVehicleRequirementJson;
    }
}
