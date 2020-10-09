package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentEquipmentRequirement;
import ekol.orders.transportOrder.domain.EquipmentType;

public final class OrderShipmentEquipmentRequirementBuilder {
    private boolean deleted;
    private Long id;
    private UtcDateTime lastUpdated;
    private OrderShipment shipment;
    private String lastUpdatedBy;
    private EquipmentType equipment;
    private Integer count;
    private UtcDateTime deletedAt;

    private OrderShipmentEquipmentRequirementBuilder() {
    }

    public static OrderShipmentEquipmentRequirementBuilder anOrderShipmentEquipmentRequirement() {
        return new OrderShipmentEquipmentRequirementBuilder();
    }

    public OrderShipmentEquipmentRequirementBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderShipmentEquipmentRequirementBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentEquipmentRequirementBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderShipmentEquipmentRequirementBuilder withShipment(OrderShipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public OrderShipmentEquipmentRequirementBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderShipmentEquipmentRequirementBuilder withEquipment(EquipmentType equipment) {
        this.equipment = equipment;
        return this;
    }

    public OrderShipmentEquipmentRequirementBuilder withCount(Integer count) {
        this.count = count;
        return this;
    }

    public OrderShipmentEquipmentRequirementBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderShipmentEquipmentRequirementBuilder but() {
        return anOrderShipmentEquipmentRequirement().withDeleted(deleted).withId(id).withLastUpdated(lastUpdated).withShipment(shipment).withLastUpdatedBy(lastUpdatedBy).withEquipment(equipment).withCount(count).withDeletedAt(deletedAt);
    }

    public OrderShipmentEquipmentRequirement build() {
        OrderShipmentEquipmentRequirement orderShipmentEquipmentRequirement = new OrderShipmentEquipmentRequirement();
        orderShipmentEquipmentRequirement.setDeleted(deleted);
        orderShipmentEquipmentRequirement.setId(id);
        orderShipmentEquipmentRequirement.setLastUpdated(lastUpdated);
        orderShipmentEquipmentRequirement.setShipment(shipment);
        orderShipmentEquipmentRequirement.setLastUpdatedBy(lastUpdatedBy);
        orderShipmentEquipmentRequirement.setEquipment(equipment);
        orderShipmentEquipmentRequirement.setCount(count);
        orderShipmentEquipmentRequirement.setDeletedAt(deletedAt);
        return orderShipmentEquipmentRequirement;
    }
}
