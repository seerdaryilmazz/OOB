package ekol.orders.transportOrder.elastic.shipment.controller;

import ekol.orders.transportOrder.elastic.shipment.config.ShipmentSearchConfig;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.elastic.shipment.model.ShipmentSearchResult;
import ekol.orders.transportOrder.elastic.shipment.service.ShipmentSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ozer on 03/10/16.
 */
@RestController
public class ShipmentSearchController {

    @Autowired
    private ShipmentSearchService shipmentSearchService;

    @RequestMapping(value = "/search/shipment", method = RequestMethod.POST)
    public ShipmentSearchResult searchShipments(@RequestBody ShipmentSearchConfig config) {
        return shipmentSearchService.searchShipments(config);
    }

    @RequestMapping(value = "/search/shipment/{shipmentId}", method = RequestMethod.GET)
    public ShipmentDocument searchShipments(@PathVariable Long shipmentId) {
        return shipmentSearchService.searchShipmentByShipmentId(shipmentId);
    }

    @RequestMapping(value = "/search/shipment/by-transport-order-id/{transportOrderId}", method = RequestMethod.GET)
    public List<ShipmentDocument> searchShipmentsOfTransportOrder(@PathVariable Long transportOrderId) {
        return shipmentSearchService.searchShipmentsByTransportId(transportOrderId);
    }
}
