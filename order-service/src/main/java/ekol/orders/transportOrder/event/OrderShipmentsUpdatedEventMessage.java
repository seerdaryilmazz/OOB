package ekol.orders.transportOrder.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;

import java.util.List;

/**
 * Created by burak on 23/11/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShipmentsUpdatedEventMessage {

    private List<ShipmentDocument> shipments;

    public static OrderShipmentsUpdatedEventMessage createWith(List<ShipmentDocument> shipments) {
        OrderShipmentsUpdatedEventMessage message = new OrderShipmentsUpdatedEventMessage();
        message.setShipments(shipments);
        return message;
    }

    public List<ShipmentDocument> getShipments() {
        return shipments;
    }

    public void setShipments(List<ShipmentDocument> shipments) {
        this.shipments = shipments;
    }
}
