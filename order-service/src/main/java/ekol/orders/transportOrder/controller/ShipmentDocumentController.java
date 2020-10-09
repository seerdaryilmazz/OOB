package ekol.orders.transportOrder.controller;

import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.elastic.shipment.service.ShipmentIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kilimci on 08/09/2017.
 */
@RestController
@RequestMapping("/shipment-document")
public class ShipmentDocumentController {

    @Autowired
    private ShipmentIndexService shipmentIndexService;

    @RequestMapping(value = {"/by-order-id/{id}"}, method = RequestMethod.GET)
    public List<ShipmentDocument> generateShipmentDocumentForTransportOrder(@PathVariable Long id) {
        return shipmentIndexService.convertTransportOrderForIndex(id);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public ShipmentDocument generateShipmentDocument(@PathVariable Long id) {
        return shipmentIndexService.convertShipmentForIndex(id);
    }
}
