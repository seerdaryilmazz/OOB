package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import ekol.orders.order.domain.dto.json.OrderShipmentEquipmentRequirementJson;

public final class OrderShipmentEquipmentRequirementJsonBuilder {
    private Long id;
    private IdCodeNameTrio equipment;
    private Integer count;

    private OrderShipmentEquipmentRequirementJsonBuilder() {
    }

    public static OrderShipmentEquipmentRequirementJsonBuilder anOrderShipmentEquipmentRequirementJson() {
        return new OrderShipmentEquipmentRequirementJsonBuilder();
    }

    public OrderShipmentEquipmentRequirementJsonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentEquipmentRequirementJsonBuilder withEquipment(IdCodeNameTrio equipment) {
        this.equipment = equipment;
        return this;
    }

    public OrderShipmentEquipmentRequirementJsonBuilder withCount(Integer count) {
        this.count = count;
        return this;
    }

    public OrderShipmentEquipmentRequirementJsonBuilder but() {
        return anOrderShipmentEquipmentRequirementJson().withId(id).withEquipment(equipment).withCount(count);
    }

    public OrderShipmentEquipmentRequirementJson build() {
        OrderShipmentEquipmentRequirementJson orderShipmentEquipmentRequirementJson = new OrderShipmentEquipmentRequirementJson();
        orderShipmentEquipmentRequirementJson.setId(id);
        orderShipmentEquipmentRequirementJson.setEquipment(equipment);
        orderShipmentEquipmentRequirementJson.setCount(count);
        return orderShipmentEquipmentRequirementJson;
    }
}
