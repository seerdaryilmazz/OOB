package ekol.location.event;


import ekol.event.annotation.ProducesEvent;
import ekol.location.domain.location.warehouse.Warehouse;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 27/11/2017.
 */
@Component
public class WarehouseUpdatedEventProducer {

    @ProducesEvent(event = "warehouse-updated")
    public WarehouseUpdatedEventMessage productWarehouseUpdatedEvent(Warehouse warehouse) {
        return WarehouseUpdatedEventMessage.createWith(warehouse);
    }

}
