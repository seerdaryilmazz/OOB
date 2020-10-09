package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.PermissionType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "VehicleRequirement")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleRequirement extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_vehicle_requirement", sequenceName = "seq_vehicle_requirement")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vehicle_requirement")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportOrderId")
    @JsonBackReference
    private TransportOrder transportOrder;

    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Convert(converter = VehicleFeature.DBConverter.class)
    private Set<VehicleFeature> vehicleFeatures = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Set<VehicleFeature> getVehicleFeatures() {
        return vehicleFeatures;
    }

    public void setVehicleFeatures(Set<VehicleFeature> vehicleFeatures) {
        this.vehicleFeatures = vehicleFeatures;
    }
}
