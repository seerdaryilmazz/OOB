package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.response.UnitLoadSpecRuleResponse;

public final class UnitLoadSpecRuleResponseBuilder {
    private boolean longLoad;
    private boolean oversizeLoad;

    private UnitLoadSpecRuleResponseBuilder() {
    }

    public static UnitLoadSpecRuleResponseBuilder anUnitLoadSpecRuleResponse() {
        return new UnitLoadSpecRuleResponseBuilder();
    }

    public UnitLoadSpecRuleResponseBuilder withLongLoad(boolean longLoad) {
        this.longLoad = longLoad;
        return this;
    }

    public UnitLoadSpecRuleResponseBuilder withOversizeLoad(boolean oversizeLoad) {
        this.oversizeLoad = oversizeLoad;
        return this;
    }

    public UnitLoadSpecRuleResponseBuilder but() {
        return anUnitLoadSpecRuleResponse().withLongLoad(longLoad).withOversizeLoad(oversizeLoad);
    }

    public UnitLoadSpecRuleResponse build() {
        UnitLoadSpecRuleResponse unitLoadSpecRuleResponse = new UnitLoadSpecRuleResponse();
        unitLoadSpecRuleResponse.setLongLoad(longLoad);
        unitLoadSpecRuleResponse.setOversizeLoad(oversizeLoad);
        return unitLoadSpecRuleResponse;
    }
}
