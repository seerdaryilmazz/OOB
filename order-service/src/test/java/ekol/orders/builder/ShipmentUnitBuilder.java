package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.transportOrder.domain.Shipment;
import ekol.orders.transportOrder.domain.ShipmentUnit;
import ekol.orders.transportOrder.domain.ShipmentUnitPackage;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public final class ShipmentUnitBuilder {

    private Long id;
    private Shipment shipment;
    private PackageType type;
    private BigDecimal totalGrossWeightInKilograms;
    private BigDecimal totalNetWeightInKilograms;
    private BigDecimal totalVolumeInCubicMeters;
    private BigDecimal totalLdm;
    private Set<ShipmentUnitPackage> shipmentUnitPackages = new HashSet<>();
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private ShipmentUnitBuilder() {
    }

    public static ShipmentUnitBuilder aShipmentUnit() {
        return new ShipmentUnitBuilder();
    }

    public ShipmentUnitBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ShipmentUnitBuilder withShipment(Shipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public ShipmentUnitBuilder withType(PackageType type) {
        this.type = type;
        return this;
    }

    public ShipmentUnitBuilder withTotalGrossWeightInKilograms(BigDecimal totalGrossWeightInKilograms) {
        this.totalGrossWeightInKilograms = totalGrossWeightInKilograms;
        return this;
    }

    public ShipmentUnitBuilder withTotalNetWeightInKilograms(BigDecimal totalNetWeightInKilograms) {
        this.totalNetWeightInKilograms = totalNetWeightInKilograms;
        return this;
    }

    public ShipmentUnitBuilder withTotalVolumeInCubicMeters(BigDecimal totalVolumeInCubicMeters) {
        this.totalVolumeInCubicMeters = totalVolumeInCubicMeters;
        return this;
    }

    public ShipmentUnitBuilder withTotalLdm(BigDecimal totalLdm) {
        this.totalLdm = totalLdm;
        return this;
    }

    public ShipmentUnitBuilder withShipmentUnitPackages(Set<ShipmentUnitPackage> shipmentUnitPackages) {
        this.shipmentUnitPackages = shipmentUnitPackages;
        return this;
    }

    public ShipmentUnitBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public ShipmentUnitBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ShipmentUnitBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ShipmentUnitBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public ShipmentUnit build() {
        ShipmentUnit shipmentUnit = new ShipmentUnit();
        shipmentUnit.setId(id);
        shipmentUnit.setShipment(shipment);
        shipmentUnit.setType(type);
        shipmentUnit.setTotalGrossWeightInKilograms(totalGrossWeightInKilograms);
        shipmentUnit.setTotalNetWeightInKilograms(totalNetWeightInKilograms);
        shipmentUnit.setTotalVolumeInCubicMeters(totalVolumeInCubicMeters);
        shipmentUnit.setTotalLdm(totalLdm);
        shipmentUnit.setShipmentUnitPackages(shipmentUnitPackages);
        shipmentUnit.setDeleted(deleted);
        shipmentUnit.setDeletedAt(deletedAt);
        shipmentUnit.setLastUpdated(lastUpdated);
        shipmentUnit.setLastUpdatedBy(lastUpdatedBy);
        return shipmentUnit;
    }
}
