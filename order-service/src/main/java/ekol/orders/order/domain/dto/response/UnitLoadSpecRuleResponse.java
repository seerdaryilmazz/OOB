package ekol.orders.order.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitLoadSpecRuleResponse {

    private boolean longLoad;
    private boolean oversizeLoad;

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
}
