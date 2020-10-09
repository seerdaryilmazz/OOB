package ekol.orders.transportOrder.elastic.shipment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.domain.TransportOrderCreatedEventInfo;


/**
 * Created by ozer on 05/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderIndexRequest extends TransportOrderCreatedEventInfo {

    private Long shipmentId;
    private FromOrTo from;
    private FromOrTo to;

    public TransportOrderIndexRequest() {
    }

    public TransportOrderIndexRequest(Long transportOrderId, Long shipmentId, FromOrTo from, FromOrTo to) {
        super(transportOrderId);
        this.shipmentId = shipmentId;
        this.from = from;
        this.to = to;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public FromOrTo getFrom() {
        return from;
    }

    public void setFrom(FromOrTo from) {
        this.from = from;
    }

    public FromOrTo getTo() {
        return to;
    }

    public void setTo(FromOrTo to) {
        this.to = to;
    }
}
