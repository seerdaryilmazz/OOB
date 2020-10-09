package ekol.orders.order.event;

import ekol.orders.order.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName="with")
public class OrderEvent {
	private Order order;
	private Operation operation;
}
