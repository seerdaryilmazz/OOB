package ekol.location.event;

import ekol.location.domain.location.warehouse.Warehouse;

/**
 * Created by kilimci on 27/11/2017.
 */
public class WarehouseUpdatedEventMessage {

    private Long id;
    private String name;

    public static WarehouseUpdatedEventMessage createWith(Warehouse warehouse){
        WarehouseUpdatedEventMessage message = new WarehouseUpdatedEventMessage();
        message.setId(warehouse.getId());
        message.setName(warehouse.getName());
        return message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
