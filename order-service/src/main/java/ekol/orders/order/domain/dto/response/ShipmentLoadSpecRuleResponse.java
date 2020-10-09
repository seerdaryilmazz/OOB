package ekol.orders.order.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentLoadSpecRuleResponse {

    private boolean heavyLoad;
    private boolean valuableLoad;

    public boolean isHeavyLoad() {
        return heavyLoad;
    }

    public void setHeavyLoad(boolean heavyLoad) {
        this.heavyLoad = heavyLoad;
    }

    public boolean isValuableLoad() {
        return valuableLoad;
    }

    public void setValuableLoad(boolean valuableLoad) {
        this.valuableLoad = valuableLoad;
    }
}
