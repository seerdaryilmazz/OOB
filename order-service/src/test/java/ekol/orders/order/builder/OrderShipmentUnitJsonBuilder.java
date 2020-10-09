package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import ekol.orders.order.domain.dto.json.OrderShipmentUnitJson;

import java.math.BigDecimal;

public final class OrderShipmentUnitJsonBuilder {
    private Long id;
    private Integer quantity;
    private IdCodeNameTrio packageType;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal height;
    private Integer stackSize;
    private String templateId;

    private OrderShipmentUnitJsonBuilder() {
    }

    public static OrderShipmentUnitJsonBuilder anOrderShipmentUnitJson() {
        return new OrderShipmentUnitJsonBuilder();
    }

    public OrderShipmentUnitJsonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentUnitJsonBuilder withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderShipmentUnitJsonBuilder withPackageType(IdCodeNameTrio packageType) {
        this.packageType = packageType;
        return this;
    }

    public OrderShipmentUnitJsonBuilder withWidth(BigDecimal width) {
        this.width = width;
        return this;
    }

    public OrderShipmentUnitJsonBuilder withLength(BigDecimal length) {
        this.length = length;
        return this;
    }

    public OrderShipmentUnitJsonBuilder withHeight(BigDecimal height) {
        this.height = height;
        return this;
    }

    public OrderShipmentUnitJsonBuilder withStackSize(Integer stackSize) {
        this.stackSize = stackSize;
        return this;
    }

    public OrderShipmentUnitJsonBuilder withTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public OrderShipmentUnitJsonBuilder but() {
        return anOrderShipmentUnitJson().withId(id).withQuantity(quantity).withPackageType(packageType).withWidth(width)
                .withLength(length).withHeight(height).withStackSize(stackSize).withTemplateId(templateId);
    }

    public OrderShipmentUnitJson build() {
        OrderShipmentUnitJson orderShipmentUnitJson = new OrderShipmentUnitJson();
        orderShipmentUnitJson.setId(id);
        orderShipmentUnitJson.setQuantity(quantity);
        orderShipmentUnitJson.setPackageType(packageType);
        orderShipmentUnitJson.setWidth(width);
        orderShipmentUnitJson.setLength(length);
        orderShipmentUnitJson.setHeight(height);
        orderShipmentUnitJson.setStackSize(stackSize);
        orderShipmentUnitJson.setTemplateId(templateId);
        return orderShipmentUnitJson;
    }
}
