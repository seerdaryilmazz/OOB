package ekol.location.domain.location.warehouse;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.location.enumeration.WarehouseRampSelectionType;
import ekol.location.domain.location.enumeration.WarehouseZoneType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by burak on 01/02/17.
 */
@Entity
@Table(name = "warehouse_zone")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseZone extends BaseEntity{

    @Id
    @SequenceGenerator(name = "seq_warehouse_zone", sequenceName = "seq_warehouse_zone")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_warehouse_zone")
    private Long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private WarehouseZoneType type;
    private BigDecimal area;
    private BigDecimal height;
    private Integer floorNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId")
    @JsonBackReference
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    private WarehouseRampSelectionType rampSelectionForGoodsIn;

    @Enumerated(EnumType.STRING)
    private WarehouseRampSelectionType rampSelectionForGoodsOut;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "WAREHOUSE_ZONE_GOODSIN_RAMP",
            joinColumns = {@JoinColumn(name = "ZONE_ID") },
            inverseJoinColumns = { @JoinColumn(name = "RAMP_ID") })
    @OrderColumn(name = "sortIndex")
    private List<WarehouseRamp> goodsInRamps;


    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "WAREHOUSE_ZONE_GOODSOUT_RAMP",
            joinColumns = {@JoinColumn(name = "ZONE_ID") },
            inverseJoinColumns = { @JoinColumn(name = "RAMP_ID") })
    @OrderColumn(name = "sortIndex")
    private List<WarehouseRamp> goodsOutRamps;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WarehouseZoneType getType() {
        return type;
    }

    public void setType(WarehouseZoneType type) {
        this.type = type;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
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

    public WarehouseRampSelectionType getRampSelectionForGoodsIn() {
        return rampSelectionForGoodsIn;
    }

    public void setRampSelectionForGoodsIn(WarehouseRampSelectionType rampSelectionForGoodsIn) {
        this.rampSelectionForGoodsIn = rampSelectionForGoodsIn;
    }

    public WarehouseRampSelectionType getRampSelectionForGoodsOut() {
        return rampSelectionForGoodsOut;
    }

    public void setRampSelectionForGoodsOut(WarehouseRampSelectionType rampSelectionForGoodsOut) {
        this.rampSelectionForGoodsOut = rampSelectionForGoodsOut;
    }

    public List<WarehouseRamp> getGoodsInRamps() {
        return goodsInRamps;
    }

    public void setGoodsInRamps(List<WarehouseRamp> goodsInRamps) {
        this.goodsInRamps = goodsInRamps;
    }

    public List<WarehouseRamp> getGoodsOutRamps() {
        return goodsOutRamps;
    }

    public void setGoodsOutRamps(List<WarehouseRamp> goodsOutRamps) {
        this.goodsOutRamps = goodsOutRamps;
    }
}
