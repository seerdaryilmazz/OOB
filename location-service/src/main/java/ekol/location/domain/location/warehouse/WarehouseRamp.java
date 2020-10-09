package ekol.location.domain.location.warehouse;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.location.enumeration.WarehouseRampProperty;

/**
 * Created by burak on 01/02/17.
 */
@Entity
@Table(name = "warehouse_ramp")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseRamp extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_warehouse_ramp", sequenceName = "seq_warehouse_ramp")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_warehouse_ramp")
    private Long id;

    private Integer rampNo;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @Enumerated(EnumType.STRING)
    private WarehouseRampProperty property;
    private Integer floorNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId")
    @JsonBackReference
    private Warehouse warehouse;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRampNo() {
        return rampNo;
    }

    public void setRampNo(Integer rampNo) {
        this.rampNo = rampNo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public WarehouseRampProperty getProperty() {
        return property;
    }

    public void setProperty(WarehouseRampProperty property) {
        this.property = property;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

}
