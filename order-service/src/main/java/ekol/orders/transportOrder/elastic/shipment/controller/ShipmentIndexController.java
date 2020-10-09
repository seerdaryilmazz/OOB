package ekol.orders.transportOrder.elastic.shipment.controller;

import ekol.orders.transportOrder.elastic.shipment.service.ShipmentIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ozer on 03/10/16.
 */
public class ShipmentIndexController {

    private ShipmentIndexService shipmentIndexService;

    public void reIndexAllShipments() {
        shipmentIndexService.deleteAndBuildIndex();
    }

}
