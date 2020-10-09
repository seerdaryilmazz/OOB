package ekol.location.domain.location.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.location.enumeration.WarehouseRampProperty;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.domain.location.warehouse.WarehouseRamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by burak on 01/02/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseRampGroup extends BaseEntity {

    private Integer rampFrom;
    private Integer rampTo;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private WarehouseRampProperty property;
    private Integer floorNumber;

    @JsonBackReference
    private Warehouse warehouse;

    public WarehouseRampGroup() {

    }

    public WarehouseRampGroup(WarehouseRamp warehouseRamp) {
        this.setRampFrom(warehouseRamp.getRampNo());
        this.setRampTo(warehouseRamp.getRampNo());
        this.setActive(warehouseRamp.isActive());
        this.setProperty(warehouseRamp.getProperty());
        this.setFloorNumber(warehouseRamp.getFloorNumber());
        this.setDeleted(warehouseRamp.isDeleted());
        this.setWarehouse(warehouseRamp.getWarehouse());
    }

    public Integer getRampFrom() {
        return rampFrom;
    }

    public void setRampFrom(Integer rampFrom) {
        this.rampFrom = rampFrom;
    }

    public Integer getRampTo() {
        return rampTo;
    }

    public void setRampTo(Integer rampTo) {
        this.rampTo = rampTo;
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

    public String retrieveRampNumberIntervalAsString() {
        if ( rampFrom.compareTo(rampTo) == 0) {
            return rampFrom + "";
        } else {
            return rampFrom + "-" + rampTo;
        }
    }
}
