package ekol.orders.order.domain;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.AdrClassDetails;
import ekol.orders.lookup.domain.AdrPackageType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Where(clause = "deleted = 0")
public class OrderShipmentAdr extends BaseEntity{
    @Id
    @SequenceGenerator(name = "seq_order_shipment_adr", sequenceName = "seq_order_shipment_adr")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment_adr")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    private OrderShipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adrClassDetailsId")
    private AdrClassDetails adrClassDetails;

    @Column
    private Integer quantity;

    @Column
    private Integer innerQuantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "packageTypeId")
    private AdrPackageType packageType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "innerPackageTypeId")
    private AdrPackageType innerPackageType;

    @Column
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private AdrUnit unit;

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

    public AdrClassDetails getAdrClassDetails() {
        return adrClassDetails;
    }

    public void setAdrClassDetails(AdrClassDetails adrClassDetails) {
        this.adrClassDetails = adrClassDetails;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getInnerQuantity() {
        return innerQuantity;
    }

    public void setInnerQuantity(Integer innerQuantity) {
        this.innerQuantity = innerQuantity;
    }

    public AdrPackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(AdrPackageType packageType) {
        this.packageType = packageType;
    }

    public AdrPackageType getInnerPackageType() {
        return innerPackageType;
    }

    public void setInnerPackageType(AdrPackageType innerPackageType) {
        this.innerPackageType = innerPackageType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AdrUnit getUnit() {
        return unit;
    }

    public void setUnit(AdrUnit unit) {
        this.unit = unit;
    }
}
