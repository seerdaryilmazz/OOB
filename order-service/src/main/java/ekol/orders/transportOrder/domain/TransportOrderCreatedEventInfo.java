package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderCreatedEventInfo {

    private Long transportOrderId;

    public TransportOrderCreatedEventInfo() {
    }

    public TransportOrderCreatedEventInfo(Long transportOrderId) {
        this.transportOrderId = transportOrderId;
    }

    public Long getTransportOrderId() {
        return transportOrderId;
    }

    public void setTransportOrderId(Long transportOrderId) {
        this.transportOrderId = transportOrderId;
    }
}
