package ekol.orders.transportOrder.event;

import ekol.event.annotation.ProducesEvent;
import ekol.orders.transportOrder.domain.TransportOrder;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 09/08/2017.
 */
@Component
public class OrderCreatedEventProducer {

    @ProducesEvent(event = "order-created")
    public OrderCreatedEventMessage produce(TransportOrder transportOrder) {
        return OrderCreatedEventMessage.createWith(transportOrder);
    }
}
