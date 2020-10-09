package ekol.orders.order.domain;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.PackageType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Where(clause = "deleted = 0")
public class OrderShipmentUnit extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_order_shipment_unit", sequenceName = "seq_order_shipment_unit")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment_unit")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    private OrderShipment shipment;

    @Column
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packageTypeId")
    private PackageType packageType;

    @Column
    private BigDecimal width;
    @Column
    private BigDecimal length;
    @Column
    private BigDecimal height;
    @Column
    private Integer stackSize; // = 0: max stack (truck height/height), = 1: means non stackable, > 1: stack size
    @Column
    private String templateId;

    @Transient
    private BigDecimal volume;
    @Transient
    private BigDecimal ldm;
    @Transient
    private boolean longLoad;
    @Transient
    private boolean oversizeLoad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderShipment getShipment() {
        return shipment;
    }

    public void setShipment(OrderShipment shipment) {
        this.shipment = shipment;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Integer getStackSize() {
        return stackSize;
    }

    public void setStackSize(Integer stackSize) {
        this.stackSize = stackSize;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getLdm() {
        return ldm;
    }

    public void setLdm(BigDecimal ldm) {
        this.ldm = ldm;
    }

    public boolean isLongLoad() {
        return longLoad;
    }

    public void setLongLoad(boolean longLoad) {
        this.longLoad = longLoad;
    }

    public boolean isOversizeLoad() {
        return oversizeLoad;
    }

    public void setOversizeLoad(boolean oversizeLoad) {
        this.oversizeLoad = oversizeLoad;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public boolean canCalculateLdm(){
        return getWidth() != null && getLength() != null &&
                getStackSize() != null && getQuantity() != null && (getStackSize() > 0 || getHeight() != null);
    }
    public Integer calculateStackSize(){
        final Integer truckHeight = 300;
        if(new Integer(0).equals(getStackSize())){
            if(getHeight() != null){
                return new BigDecimal(truckHeight).divide(getHeight(), 0, RoundingMode.FLOOR).intValue();
            }else{
                return null;
            }
        }else{
            return getStackSize();
        }
    }
}
