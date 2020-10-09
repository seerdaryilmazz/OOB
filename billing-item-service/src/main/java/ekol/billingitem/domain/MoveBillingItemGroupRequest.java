package ekol.billingitem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveBillingItemGroupRequest {

    private BillingItemGroup parent;

    public BillingItemGroup getParent() {
        return parent;
    }

    public void setParent(BillingItemGroup parent) {
        this.parent = parent;
    }
}
