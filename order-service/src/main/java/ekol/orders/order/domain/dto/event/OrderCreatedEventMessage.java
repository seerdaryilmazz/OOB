package ekol.orders.order.domain.dto.event;

import ekol.orders.order.domain.Order;
import lombok.Data;

@Data
public class OrderCreatedEventMessage {

    private Long orderId;
    private String status;

    public static OrderCreatedEventMessage with(Order order){
        OrderCreatedEventMessage message = new OrderCreatedEventMessage();
        message.setOrderId(order.getId());
        message.setStatus(order.getStatus().name());
        return message;
    }
}
