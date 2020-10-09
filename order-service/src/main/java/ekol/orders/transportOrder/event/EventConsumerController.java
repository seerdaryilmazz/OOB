package ekol.orders.transportOrder.event;


import ekol.event.annotation.ConsumesWebEvent;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.elastic.shipment.service.ShipmentIndexService;
import ekol.orders.transportOrder.service.BarcodeService;
import ekol.orders.transportOrder.service.TransportOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kilimci on 11/08/2017.
 */
@RestController
@RequestMapping("/event-consumer")
public class EventConsumerController {

    @Autowired
    private ShipmentIndexService shipmentIndexService;
    @Autowired
    private TransportOrderService transportOrderService;
    @Autowired
    private BarcodeService barcodeService;
    @Autowired
    private OrderShipmentsUpdatedEventProducer orderShipmentsUpdatedEventProducer;


    @RequestMapping(value = "/order-created", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "order-created", name = "index transport order on search engine")
    public void consumeOrderCreated(@RequestBody OrderCreatedEventMessage message){
        shipmentIndexService.indexTransportOrder(message.getOrder().getId());
    }

    @RequestMapping(value = "/order-rules-executed", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "order-rules-executed", name = "save and index rule engine results")
    public void consumeRulesExecuted(@RequestBody OrderRulesExecutedEventMessage message){
        transportOrderService.processRuleExecutions(message);
        List<ShipmentDocument> shipmentDocuments = shipmentIndexService.indexTransportOrder(message.getOrderResult().getOrderId());
        orderShipmentsUpdatedEventProducer.produce(shipmentDocuments);
    }

    @RequestMapping(value = "/trip-plan-created", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "trip-plan-created", name = "generate barcodes")
    public void generateBarcodes(@RequestBody TripPlanCreatedEventMessage message){
        barcodeService.processTripPlanCreated(message);
        transportOrderService.processTripPlanCreated(message);
    }

    @RequestMapping(value = "/shipment-collected", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "shipment-collected", name = "update shipment status as collected")
    public void consumeShipmentCollected(@RequestBody ShipmentStatusChangeMessage message){
        transportOrderService.handleShipmentCollected(message);
    }

    @RequestMapping(value = "/shipment-delivered", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "shipment-delivered", name = "update shipment status as delivered")
    public void consumeShipmentDelivered(@RequestBody ShipmentStatusChangeMessage message){
        transportOrderService.handleShipmentDelivered(message);
    }
    
}
