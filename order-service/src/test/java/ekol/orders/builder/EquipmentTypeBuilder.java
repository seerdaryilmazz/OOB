package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.transportOrder.domain.EquipmentType;

public final class EquipmentTypeBuilder {

    private Long id;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private EquipmentTypeBuilder() {
    }

    public static EquipmentTypeBuilder anEquipmentType() {
        return new EquipmentTypeBuilder();
    }

    public EquipmentTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EquipmentTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public EquipmentTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EquipmentTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public EquipmentTypeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public EquipmentTypeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public EquipmentTypeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public EquipmentType build() {
        EquipmentType equipmentType = new EquipmentType();
        equipmentType.setId(id);
        equipmentType.setCode(code);
        equipmentType.setName(name);
        equipmentType.setDeleted(deleted);
        equipmentType.setDeletedAt(deletedAt);
        equipmentType.setLastUpdated(lastUpdated);
        equipmentType.setLastUpdatedBy(lastUpdatedBy);
        return equipmentType;
    }
}
