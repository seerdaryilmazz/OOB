package ekol.orders.order.domain.dto.event;

import ekol.orders.order.domain.Order;
import lombok.Data;

@Data
public class OrderUpdatedEventMessage {

    private Long orderId;
    private String status;

    public static OrderUpdatedEventMessage with(Order order){
        OrderUpdatedEventMessage message = new OrderUpdatedEventMessage();
        message.setOrderId(order.getId());
        message.setStatus(order.getStatus().name());
        return message;
    }
}
