package ekol.orders.order.builder;

import ekol.orders.order.domain.AdrUnit;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import ekol.orders.order.domain.dto.json.OrderShipmentAdrJson;

import java.math.BigDecimal;

public final class OrderShipmentAdrJsonBuilder {
    private Long id;
    private Long adrClassDetailsId;
    private Integer quantity;
    private Integer innerQuantity;
    private IdCodeNameTrio packageType;
    private IdCodeNameTrio innerPackageType;
    private BigDecimal amount;
    private AdrUnit unit;

    private OrderShipmentAdrJsonBuilder() {
    }

    public static OrderShipmentAdrJsonBuilder anOrderShipmentAdrJson() {
        return new OrderShipmentAdrJsonBuilder();
    }

    public OrderShipmentAdrJsonBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    public OrderShipmentAdrJsonBuilder withAdrClassDetailsId(Long adrClassDetailsId) {
        this.adrClassDetailsId = adrClassDetailsId;
        return this;
    }

    public OrderShipmentAdrJsonBuilder withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderShipmentAdrJsonBuilder withInnerQuantity(Integer innerQuantity) {
        this.innerQuantity = innerQuantity;
        return this;
    }

    public OrderShipmentAdrJsonBuilder withPackageType(IdCodeNameTrio packageType) {
        this.packageType = packageType;
        return this;
    }

    public OrderShipmentAdrJsonBuilder withInnerPackageType(IdCodeNameTrio innerPackageType) {
        this.innerPackageType = innerPackageType;
        return this;
    }

    public OrderShipmentAdrJsonBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public OrderShipmentAdrJsonBuilder withUnit(AdrUnit unit) {
        this.unit = unit;
        return this;
    }

    public OrderShipmentAdrJsonBuilder but() {
        return anOrderShipmentAdrJson().withId(id).withAdrClassDetailsId(adrClassDetailsId)
                .withQuantity(quantity).withInnerQuantity(innerQuantity).withPackageType(packageType)
                .withInnerPackageType(innerPackageType).withAmount(amount).withUnit(unit);
    }

    public OrderShipmentAdrJson build() {
        OrderShipmentAdrJson orderShipmentAdrJson = new OrderShipmentAdrJson();
        orderShipmentAdrJson.setId(id);
        orderShipmentAdrJson.setAdrClassDetailsId(adrClassDetailsId);
        orderShipmentAdrJson.setQuantity(quantity);
        orderShipmentAdrJson.setInnerQuantity(innerQuantity);
        orderShipmentAdrJson.setPackageType(packageType);
        orderShipmentAdrJson.setInnerPackageType(innerPackageType);
        orderShipmentAdrJson.setAmount(amount);
        orderShipmentAdrJson.setUnit(unit);
        return orderShipmentAdrJson;
    }
}
