package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.response.ShipmentLoadSpecRuleResponse;

public final class ShipmentLoadSpecRuleResponseBuilder {
    private boolean heavyLoad;
    private boolean valuableLoad;

    private ShipmentLoadSpecRuleResponseBuilder() {
    }

    public static ShipmentLoadSpecRuleResponseBuilder aShipmentLoadSpecRuleResponse() {
        return new ShipmentLoadSpecRuleResponseBuilder();
    }

    public ShipmentLoadSpecRuleResponseBuilder withHeavyLoad(boolean heavyLoad) {
        this.heavyLoad = heavyLoad;
        return this;
    }

    public ShipmentLoadSpecRuleResponseBuilder withValuableLoad(boolean valuableLoad) {
        this.valuableLoad = valuableLoad;
        return this;
    }

    public ShipmentLoadSpecRuleResponseBuilder but() {
        return aShipmentLoadSpecRuleResponse().withHeavyLoad(heavyLoad).withValuableLoad(valuableLoad);
    }

    public ShipmentLoadSpecRuleResponse build() {
        ShipmentLoadSpecRuleResponse shipmentLoadSpecRuleResponse = new ShipmentLoadSpecRuleResponse();
        shipmentLoadSpecRuleResponse.setHeavyLoad(heavyLoad);
        shipmentLoadSpecRuleResponse.setValuableLoad(valuableLoad);
        return shipmentLoadSpecRuleResponse;
    }
}
