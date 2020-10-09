package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.PermissionType;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.domain.VehicleFeature;
import ekol.orders.transportOrder.domain.VehicleRequirement;
import ekol.orders.transportOrder.domain.VehicleType;

import java.util.HashSet;
import java.util.Set;

public final class VehicleRequirementBuilder {

    private Long id;
    private TransportOrder transportOrder;
    private PermissionType permissionType;
    private VehicleType vehicleType;
    private Set<VehicleFeature> vehicleFeatures = new HashSet<>();

    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private VehicleRequirementBuilder() {
    }

    public static VehicleRequirementBuilder aVehicleRequirement() {
        return new VehicleRequirementBuilder();
    }

    public VehicleRequirementBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public VehicleRequirementBuilder withTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
        return this;
    }

    public VehicleRequirementBuilder withPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
        return this;
    }

    public VehicleRequirementBuilder withVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public VehicleRequirementBuilder withVehicleFeatures(Set<VehicleFeature> vehicleFeatures) {
        this.vehicleFeatures = vehicleFeatures;
        return this;
    }

    public VehicleRequirementBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public VehicleRequirementBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public VehicleRequirementBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public VehicleRequirementBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public VehicleRequirement build() {
        VehicleRequirement vehicleRequirement = new VehicleRequirement();
        vehicleRequirement.setId(id);
        vehicleRequirement.setTransportOrder(transportOrder);
        vehicleRequirement.setPermissionType(permissionType);
        vehicleRequirement.setVehicleType(vehicleType);
        vehicleRequirement.setVehicleFeatures(vehicleFeatures);
        vehicleRequirement.setDeleted(deleted);
        vehicleRequirement.setDeletedAt(deletedAt);
        vehicleRequirement.setLastUpdated(lastUpdated);
        vehicleRequirement.setLastUpdatedBy(lastUpdatedBy);
        return vehicleRequirement;
    }
}
