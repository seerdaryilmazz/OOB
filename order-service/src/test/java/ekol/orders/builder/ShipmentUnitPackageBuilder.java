package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.transportOrder.domain.ShipmentUnit;
import ekol.orders.transportOrder.domain.ShipmentUnitPackage;

import java.math.BigDecimal;

public final class ShipmentUnitPackageBuilder {

    private Long id;
    private ShipmentUnit shipmentUnit;
    private Integer count;
    private BigDecimal lengthInCentimeters;
    private BigDecimal widthInCentimeters;
    private BigDecimal heightInCentimeters;
    private Integer stackSize;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private ShipmentUnitPackageBuilder() {
    }

    public static ShipmentUnitPackageBuilder aShipmentUnitPackage() {
        return new ShipmentUnitPackageBuilder();
    }

    public ShipmentUnitPackageBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ShipmentUnitPackageBuilder withShipmentUnit(ShipmentUnit shipmentUnit) {
        this.shipmentUnit = shipmentUnit;
        return this;
    }

    public ShipmentUnitPackageBuilder withCount(Integer count) {
        this.count = count;
        return this;
    }

    public ShipmentUnitPackageBuilder withLengthInCentimeters(BigDecimal lengthInCentimeters) {
        this.lengthInCentimeters = lengthInCentimeters;
        return this;
    }

    public ShipmentUnitPackageBuilder withWidthInCentimeters(BigDecimal widthInCentimeters) {
        this.widthInCentimeters = widthInCentimeters;
        return this;
    }

    public ShipmentUnitPackageBuilder withHeightInCentimeters(BigDecimal heightInCentimeters) {
        this.heightInCentimeters = heightInCentimeters;
        return this;
    }

    public ShipmentUnitPackageBuilder withStackSize(Integer stackSize) {
        this.stackSize = stackSize;
        return this;
    }

    public ShipmentUnitPackageBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public ShipmentUnitPackageBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ShipmentUnitPackageBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ShipmentUnitPackageBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public ShipmentUnitPackage build() {
        ShipmentUnitPackage shipmentUnitPackage = new ShipmentUnitPackage();
        shipmentUnitPackage.setId(id);
        shipmentUnitPackage.setShipmentUnit(shipmentUnit);
        shipmentUnitPackage.setCount(count);
        shipmentUnitPackage.setLengthInCentimeters(lengthInCentimeters);
        shipmentUnitPackage.setWidthInCentimeters(widthInCentimeters);
        shipmentUnitPackage.setHeightInCentimeters(heightInCentimeters);
        shipmentUnitPackage.setStackSize(stackSize);
        shipmentUnitPackage.setDeleted(deleted);
        shipmentUnitPackage.setDeletedAt(deletedAt);
        shipmentUnitPackage.setLastUpdated(lastUpdated);
        shipmentUnitPackage.setLastUpdatedBy(lastUpdatedBy);
        return shipmentUnitPackage;
    }
}
