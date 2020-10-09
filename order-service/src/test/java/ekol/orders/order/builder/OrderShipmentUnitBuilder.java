package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentUnit;

import java.math.BigDecimal;

public final class OrderShipmentUnitBuilder {
    private boolean deleted;
    private Long id;
    private OrderShipment shipment;
    private Integer quantity;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private PackageType packageType;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal height;
    private UtcDateTime deletedAt;
    private Integer stackSize;
    private BigDecimal volume;
    private BigDecimal ldm;
    private boolean longLoad;
    private boolean oversizeLoad;
    private String templateId;

    private OrderShipmentUnitBuilder() {
    }

    public static OrderShipmentUnitBuilder anOrderShipmentUnit() {
        return new OrderShipmentUnitBuilder();
    }

    public OrderShipmentUnitBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderShipmentUnitBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentUnitBuilder withShipment(OrderShipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public OrderShipmentUnitBuilder withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderShipmentUnitBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderShipmentUnitBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderShipmentUnitBuilder withPackageType(PackageType packageType) {
        this.packageType = packageType;
        return this;
    }

    public OrderShipmentUnitBuilder withWidth(BigDecimal width) {
        this.width = width;
        return this;
    }

    public OrderShipmentUnitBuilder withLength(BigDecimal length) {
        this.length = length;
        return this;
    }

    public OrderShipmentUnitBuilder withHeight(BigDecimal height) {
        this.height = height;
        return this;
    }

    public OrderShipmentUnitBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderShipmentUnitBuilder withStackSize(Integer stackSize) {
        this.stackSize = stackSize;
        return this;
    }

    public OrderShipmentUnitBuilder withVolume(BigDecimal volume) {
        this.volume = volume;
        return this;
    }

    public OrderShipmentUnitBuilder withLdm(BigDecimal ldm) {
        this.ldm = ldm;
        return this;
    }

    public OrderShipmentUnitBuilder withLongLoad(boolean longLoad) {
        this.longLoad = longLoad;
        return this;
    }

    public OrderShipmentUnitBuilder withOversizeLoad(boolean oversizeLoad) {
        this.oversizeLoad = oversizeLoad;
        return this;
    }

    public OrderShipmentUnitBuilder withTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public OrderShipmentUnitBuilder but() {
        return anOrderShipmentUnit().withDeleted(deleted).withId(id).withShipment(shipment).withQuantity(quantity)
                .withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy).withPackageType(packageType).withWidth(width)
                .withLength(length).withHeight(height).withDeletedAt(deletedAt).withStackSize(stackSize)
                .withVolume(volume).withLdm(ldm).withLongLoad(longLoad).withOversizeLoad(oversizeLoad).withTemplateId(templateId);
    }

    public OrderShipmentUnit build() {
        OrderShipmentUnit orderShipmentUnit = new OrderShipmentUnit();
        orderShipmentUnit.setDeleted(deleted);
        orderShipmentUnit.setId(id);
        orderShipmentUnit.setShipment(shipment);
        orderShipmentUnit.setQuantity(quantity);
        orderShipmentUnit.setLastUpdated(lastUpdated);
        orderShipmentUnit.setLastUpdatedBy(lastUpdatedBy);
        orderShipmentUnit.setPackageType(packageType);
        orderShipmentUnit.setWidth(width);
        orderShipmentUnit.setLength(length);
        orderShipmentUnit.setHeight(height);
        orderShipmentUnit.setVolume(volume);
        orderShipmentUnit.setLdm(ldm);
        orderShipmentUnit.setLongLoad(longLoad);
        orderShipmentUnit.setOversizeLoad(oversizeLoad);
        orderShipmentUnit.setDeletedAt(deletedAt);
        orderShipmentUnit.setStackSize(stackSize);
        orderShipmentUnit.setTemplateId(templateId);
        return orderShipmentUnit;
    }
}
