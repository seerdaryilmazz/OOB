package ekol.orders.transportOrder.event;


import ekol.event.annotation.ProducesEvent;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kilimci on 09/08/2017.
 */
@Component
public class OrderShipmentsUpdatedEventProducer {

    @ProducesEvent(event = "order-shipments-updated")
    public OrderShipmentsUpdatedEventMessage produce(List<ShipmentDocument> shipmentDocumentList) {
        return OrderShipmentsUpdatedEventMessage.createWith(shipmentDocumentList);
    }
}
