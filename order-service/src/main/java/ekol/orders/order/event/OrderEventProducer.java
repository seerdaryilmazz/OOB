package ekol.orders.order.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.event.annotation.ProducesEvent;
import ekol.orders.order.domain.dto.json.OrderJson;

@Component
public class OrderEventProducer {

	@ProducesEvent(event = "new-order-created")
	@TransactionalEventListener(fallbackExecution = true, condition="#event.operation == T(ekol.orders.order.event.Operation).CREATE")
	public OrderJson orderCreateEventProduce(OrderEvent event) {
		return OrderJson.fromEntity(event.getOrder());
	}

	@ProducesEvent(event = "existing-order-updated")
	@TransactionalEventListener(fallbackExecution = true, condition="#event.operation == T(ekol.orders.order.event.Operation).UPDATE" )
	public OrderJson orderUpdateEventProduce(OrderEvent event) {
		return OrderJson.fromEntity(event.getOrder());
	}
}
