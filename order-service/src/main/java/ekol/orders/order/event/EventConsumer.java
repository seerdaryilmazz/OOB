package ekol.orders.order.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.annotation.ConsumesWebEvent;
import ekol.orders.order.domain.dto.json.OrderJson;
import ekol.orders.search.service.OrderIndexService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class EventConsumer {

    private OrderIndexService orderIndexService;

    @PostMapping("/new-order-created")
    @ConsumesWebEvent(event = "new-order-created", name = "index order on search engine")
    public void consumeOrderCreated(@RequestBody OrderJson orderMessage){
        orderIndexService.indexOrder(orderMessage.getId());
    }

}
