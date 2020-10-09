package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.AdrClassDetails;
import ekol.orders.lookup.domain.AdrPackageType;
import ekol.orders.order.domain.AdrUnit;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentAdr;

import java.math.BigDecimal;

public final class OrderShipmentAdrBuilder {
    private Long id;
    private boolean deleted;
    private OrderShipment shipment;
    private AdrClassDetails adrClassDetails;
    private Integer quantity;
    private Integer innerQuantity;
    private UtcDateTime lastUpdated;
    private AdrPackageType packageType;
    private String lastUpdatedBy;
    private AdrPackageType innerPackageType;
    private BigDecimal amount;
    private AdrUnit unit;
    private UtcDateTime deletedAt;

    private OrderShipmentAdrBuilder() {
    }

    public static OrderShipmentAdrBuilder anOrderShipmentAdr() {
        return new OrderShipmentAdrBuilder();
    }

    public OrderShipmentAdrBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentAdrBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderShipmentAdrBuilder withShipment(OrderShipment shipment) {
        this.shipment = shipment;
        return this;
    }
    public OrderShipmentAdrBuilder withAdrClassDetails(AdrClassDetails adrClassDetails) {
        this.adrClassDetails = adrClassDetails;
        return this;
    }

    public OrderShipmentAdrBuilder withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderShipmentAdrBuilder withInnerQuantity(Integer innerQuantity) {
        this.innerQuantity = innerQuantity;
        return this;
    }

    public OrderShipmentAdrBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderShipmentAdrBuilder withPackageType(AdrPackageType packageType) {
        this.packageType = packageType;
        return this;
    }

    public OrderShipmentAdrBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderShipmentAdrBuilder withInnerPackageType(AdrPackageType innerPackageType) {
        this.innerPackageType = innerPackageType;
        return this;
    }

    public OrderShipmentAdrBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public OrderShipmentAdrBuilder withUnit(AdrUnit unit) {
        this.unit = unit;
        return this;
    }

    public OrderShipmentAdrBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderShipmentAdrBuilder but() {
        return anOrderShipmentAdr().withId(id).withDeleted(deleted).withShipment(shipment).withAdrClassDetails(adrClassDetails)
                .withQuantity(quantity).withInnerQuantity(innerQuantity).withLastUpdated(lastUpdated)
                .withPackageType(packageType).withLastUpdatedBy(lastUpdatedBy).withInnerPackageType(innerPackageType)
                .withAmount(amount).withUnit(unit).withDeletedAt(deletedAt);
    }

    public OrderShipmentAdr build() {
        OrderShipmentAdr orderShipmentAdr = new OrderShipmentAdr();
        orderShipmentAdr.setId(id);
        orderShipmentAdr.setDeleted(deleted);
        orderShipmentAdr.setShipment(shipment);
        orderShipmentAdr.setAdrClassDetails(adrClassDetails);
        orderShipmentAdr.setQuantity(quantity);
        orderShipmentAdr.setInnerQuantity(innerQuantity);
        orderShipmentAdr.setLastUpdated(lastUpdated);
        orderShipmentAdr.setPackageType(packageType);
        orderShipmentAdr.setLastUpdatedBy(lastUpdatedBy);
        orderShipmentAdr.setInnerPackageType(innerPackageType);
        orderShipmentAdr.setAmount(amount);
        orderShipmentAdr.setUnit(unit);
        orderShipmentAdr.setDeletedAt(deletedAt);
        return orderShipmentAdr;
    }
}
