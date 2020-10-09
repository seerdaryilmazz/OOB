package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.PackageType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ShipmentUnit")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentUnit extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_shipment_unit", sequenceName = "seq_shipment_unit")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_shipment_unit")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    @JsonBackReference
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId")
    private PackageType type;

    @Column(name = "totalGrossWeightKg")
    private BigDecimal totalGrossWeightInKilograms;

    @Column(name = "totalNetWeightKg")
    private BigDecimal totalNetWeightInKilograms;

    @Column(name = "totalVolumeMtr3")
    private BigDecimal totalVolumeInCubicMeters;

    private BigDecimal totalLdm;

    @OneToMany(mappedBy = "shipmentUnit")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<ShipmentUnitPackage> shipmentUnitPackages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public PackageType getType() {
        return type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    public BigDecimal getTotalGrossWeightInKilograms() {
        return totalGrossWeightInKilograms;
    }

    public void setTotalGrossWeightInKilograms(BigDecimal totalGrossWeightInKilograms) {
        this.totalGrossWeightInKilograms = totalGrossWeightInKilograms;
    }

    public BigDecimal getTotalNetWeightInKilograms() {
        return totalNetWeightInKilograms;
    }

    public void setTotalNetWeightInKilograms(BigDecimal totalNetWeightInKilograms) {
        this.totalNetWeightInKilograms = totalNetWeightInKilograms;
    }

    public BigDecimal getTotalVolumeInCubicMeters() {
        return totalVolumeInCubicMeters;
    }

    public void setTotalVolumeInCubicMeters(BigDecimal totalVolumeInCubicMeters) {
        this.totalVolumeInCubicMeters = totalVolumeInCubicMeters;
    }

    public BigDecimal getTotalLdm() {
        return totalLdm;
    }

    public void setTotalLdm(BigDecimal totalLdm) {
        this.totalLdm = totalLdm;
    }

    public Set<ShipmentUnitPackage> getShipmentUnitPackages() {
        return shipmentUnitPackages;
    }

    public void setShipmentUnitPackages(Set<ShipmentUnitPackage> shipmentUnitPackages) {
        this.shipmentUnitPackages = shipmentUnitPackages;
    }
}
