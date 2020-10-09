package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ShipmentUnitPackage")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentUnitPackage extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_shipment_unit_package", sequenceName = "seq_shipment_unit_package")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_shipment_unit_package")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentUnitId")
    @JsonBackReference
    private ShipmentUnit shipmentUnit;

    private Integer count;

    @Column(name = "lengthCm")
    private BigDecimal lengthInCentimeters;

    @Column(name = "widthCm")
    private BigDecimal widthInCentimeters;

    @Column(name = "heightCm")
    private BigDecimal heightInCentimeters;

    private Integer stackSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShipmentUnit getShipmentUnit() {
        return shipmentUnit;
    }

    public void setShipmentUnit(ShipmentUnit shipmentUnit) {
        this.shipmentUnit = shipmentUnit;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getLengthInCentimeters() {
        return lengthInCentimeters;
    }

    public void setLengthInCentimeters(BigDecimal lengthInCentimeters) {
        this.lengthInCentimeters = lengthInCentimeters;
    }

    public BigDecimal getWidthInCentimeters() {
        return widthInCentimeters;
    }

    public void setWidthInCentimeters(BigDecimal widthInCentimeters) {
        this.widthInCentimeters = widthInCentimeters;
    }

    public BigDecimal getHeightInCentimeters() {
        return heightInCentimeters;
    }

    public void setHeightInCentimeters(BigDecimal heightInCentimeters) {
        this.heightInCentimeters = heightInCentimeters;
    }

    public Integer getStackSize() {
        return stackSize;
    }

    public void setStackSize(Integer stackSize) {
        this.stackSize = stackSize;
    }
}
