package ekol.orders.transportOrder.controller;

import com.github.fge.jsonpatch.JsonPatch;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.transportOrder.domain.Shipment;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.elastic.shipment.service.ShipmentIndexService;
import ekol.orders.transportOrder.repository.ShipmentRepository;
import ekol.orders.transportOrder.repository.TransportOrderRepository;
import ekol.orders.transportOrder.service.TransportOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transport-order")
public class TransportOrderApiController {

    @Autowired
    private TransportOrderService transportOrderService;

    @Autowired
    private TransportOrderRepository transportOrderRepository;

    @Autowired
    private ShipmentIndexService shipmentIndexService;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<TransportOrder> findAllTransportOrders() {
        return transportOrderRepository.findAllWithDetailsDistinctBy();
    }

    @RequestMapping(value = "/{transportOrderId}", method = RequestMethod.GET)
    public TransportOrder findTransportOrder(@PathVariable Long transportOrderId) {
        return transportOrderService.findWithDetailsById(transportOrderId);
    }

    @RequestMapping(value = "/by-shipment", method = RequestMethod.GET)
    public TransportOrder findTransportOrderByShipment(@RequestParam Long shipmentId) {

        Shipment shipment = shipmentRepository.findOne(shipmentId);

        if (shipment == null) {
            throw new ResourceNotFoundException("Shipment with specified id cannot be found: " + shipmentId);
        } else {
            return transportOrderService.findWithDetailsById(shipment.getTransportOrder().getId());
        }
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public TransportOrder createTransportOrder(@RequestBody TransportOrder transportOrder) {
        return transportOrderService.createTransportOrder(transportOrder);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.PUT)
    public TransportOrder updateTransportOrder(@PathVariable Long id, @RequestBody TransportOrder transportOrder) {

        if (!id.equals(transportOrder.getId())) {
            throw new BadRequestException("TransportOrder.id must be " + id + ".");
        }

        return transportOrderService.updateTransportOrder(transportOrder);
    }

    @RequestMapping(value = {"/{id}/byJson", "/{id}/byJson/"}, method = RequestMethod.PATCH)
    public void patchTransportOrder(@PathVariable Long id, @RequestBody JsonPatch patch) {

        transportOrderService.patchTransportOrder(id, patch);
    }

    @RequestMapping(value = {"/{id}/byEntity", "/{id}/byEntity/"}, method = RequestMethod.PATCH)
    public void patchTransportOrder(@PathVariable Long id, @RequestBody TransportOrder patch) {

        transportOrderService.patchTransportOrder(id, patch);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.DELETE)
    public void deleteTransportOrder(@PathVariable Long id) {
        transportOrderService.softDeleteTransportOrder(id);
    }

    @RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
    public void indexTransportOrder(@PathVariable Long id) {
        shipmentIndexService.indexTransportOrder(id);
    }

    @RequestMapping(value = "/{transportOrderId}/approve", method = RequestMethod.POST)
    public void approveTransportOrder(@PathVariable Long transportOrderId) {
        transportOrderService.approveTransportOrder(transportOrderId);
    }

    @RequestMapping(value = "/{transportOrderId}/reject", method = RequestMethod.POST)
    public void rejectTransportOrder(@PathVariable Long transportOrderId) {
        transportOrderService.rejectTransportOrder(transportOrderId);
    }

    @RequestMapping(value = "/{transportOrderId}/rule-set-details", method = RequestMethod.GET)
    public TransportOrder findTransportOrderWithRuleSetDetails(@PathVariable Long transportOrderId) {
        return transportOrderService.findWithRuleSetDetailsById(transportOrderId);
    }

    //used by ui to list all tranport order to iterate their shipments
    @RequestMapping(value = "/my-shipments", method = RequestMethod.GET)
    public List<ShipmentDocument> findMyShipments() {
        return transportOrderService.searchShipmentsOfMySubsidiary();
    }


}
