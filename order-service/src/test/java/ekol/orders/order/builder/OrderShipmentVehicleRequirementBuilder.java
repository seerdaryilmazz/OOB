package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentVehicleRequirement;
import ekol.orders.order.domain.VehicleRequirementReason;
import ekol.orders.transportOrder.domain.OrderPlanningOperationType;
import ekol.orders.transportOrder.domain.VehicleFeature;

public final class OrderShipmentVehicleRequirementBuilder {
    private boolean deleted;
    private Long id;
    private UtcDateTime lastUpdated;
    private OrderShipment shipment;
    private String lastUpdatedBy;
    private VehicleFeature requirement;
    private OrderPlanningOperationType operationType;
    private VehicleRequirementReason requirementReason;
    private UtcDateTime deletedAt;

    private OrderShipmentVehicleRequirementBuilder() {
    }

    public static OrderShipmentVehicleRequirementBuilder anOrderShipmentVehicleRequirement() {
        return new OrderShipmentVehicleRequirementBuilder();
    }

    public OrderShipmentVehicleRequirementBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder withShipment(OrderShipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder withRequirement(VehicleFeature requirement) {
        this.requirement = requirement;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder withOperationType(OrderPlanningOperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder withRequirementReason(VehicleRequirementReason requirementReason) {
        this.requirementReason = requirementReason;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderShipmentVehicleRequirementBuilder but() {
        return anOrderShipmentVehicleRequirement().withDeleted(deleted).withId(id).withLastUpdated(lastUpdated)
                .withShipment(shipment).withLastUpdatedBy(lastUpdatedBy).withRequirement(requirement)
                .withOperationType(operationType).withRequirementReason(requirementReason).withDeletedAt(deletedAt);
    }

    public OrderShipmentVehicleRequirement build() {
        OrderShipmentVehicleRequirement orderShipmentVehicleRequirement = new OrderShipmentVehicleRequirement();
        orderShipmentVehicleRequirement.setDeleted(deleted);
        orderShipmentVehicleRequirement.setId(id);
        orderShipmentVehicleRequirement.setLastUpdated(lastUpdated);
        orderShipmentVehicleRequirement.setShipment(shipment);
        orderShipmentVehicleRequirement.setLastUpdatedBy(lastUpdatedBy);
        orderShipmentVehicleRequirement.setRequirement(requirement);
        orderShipmentVehicleRequirement.setOperationType(operationType);
        orderShipmentVehicleRequirement.setRequirementReason(requirementReason);
        orderShipmentVehicleRequirement.setDeletedAt(deletedAt);
        return orderShipmentVehicleRequirement;
    }
}
