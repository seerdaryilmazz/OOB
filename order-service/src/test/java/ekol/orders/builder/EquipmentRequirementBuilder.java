package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.transportOrder.domain.EquipmentRequirement;
import ekol.orders.transportOrder.domain.EquipmentType;
import ekol.orders.transportOrder.domain.TransportOrder;


public final class EquipmentRequirementBuilder {

    private Long id;
    private TransportOrder transportOrder;
    private Integer count;
    private EquipmentType equipmentType;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private EquipmentRequirementBuilder() {
    }

    public static EquipmentRequirementBuilder anEquipmentRequirement() {
        return new EquipmentRequirementBuilder();
    }

    public EquipmentRequirementBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EquipmentRequirementBuilder withTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
        return this;
    }

    public EquipmentRequirementBuilder withCount(Integer count) {
        this.count = count;
        return this;
    }

    public EquipmentRequirementBuilder withEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
        return this;
    }

    public EquipmentRequirementBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public EquipmentRequirementBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public EquipmentRequirementBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public EquipmentRequirementBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public EquipmentRequirement build() {
        EquipmentRequirement equipmentRequirement = new EquipmentRequirement();
        equipmentRequirement.setId(id);
        equipmentRequirement.setTransportOrder(transportOrder);
        equipmentRequirement.setCount(count);
        equipmentRequirement.setEquipmentType(equipmentType);
        equipmentRequirement.setDeleted(deleted);
        equipmentRequirement.setDeletedAt(deletedAt);
        equipmentRequirement.setLastUpdated(lastUpdated);
        equipmentRequirement.setLastUpdatedBy(lastUpdatedBy);
        return equipmentRequirement;
    }
}
