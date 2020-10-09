package ekol.orders.order.controller;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.orders.order.domain.dto.json.CreateOrderRequestJson;
import ekol.orders.order.domain.dto.json.DocumentJson;
import ekol.orders.order.domain.dto.json.OrderJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateOrderStatusRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateServiceTypeRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateTruckLoadTypeRequestJson;
import ekol.orders.order.service.CreateOrderService;
import ekol.orders.order.service.ListOrderService;
import ekol.orders.order.service.OrderDocumentService;
import ekol.orders.order.service.UpdateOrderService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/order")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class OrderController {

    private CreateOrderService createOrderService;
    private ListOrderService listOrderService;
    private OrderDocumentService orderDocumentService;
    private UpdateOrderService updateOrderService;

    @Timed(value = "order.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = "order.create")
    @PostMapping(value = "")
    public List<OrderJson> createOrder(@RequestBody CreateOrderRequestJson createOrderRequest) {
        return createOrderService.create(createOrderRequest).stream().map(OrderJson::fromEntity).collect(toList());
    }

    @Timed(value = "order.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping(value = "/{id}")
    public OrderJson get(@PathVariable Long id) {
        return OrderJson.fromEntity(listOrderService.findByIdWithShipmentsOrThrowException(id));
    }
    
    @Timed(value = "order.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping(value = "")
    public OrderJson get(@RequestParam(required=false) String code, @RequestParam(required=false) String shipmentCode) {
    	if(StringUtils.isNotEmpty(code)) {
    		return OrderJson.fromEntity(listOrderService.findByCodeWithShipmentsOrThrowException(code));
    	} else if(StringUtils.isNotEmpty(shipmentCode)) {
    		return OrderJson.fromEntity(listOrderService.findByShipmentCodeWithShipmentOrThrowException(shipmentCode));
    	} else {
    		throw new BadRequestException("code or shipmentCode should be sent");
    	}
    }

    @GetMapping(value = "/{id}/documents")
    public List<DocumentJson> getDocuments(@PathVariable Long id) {
        return orderDocumentService.getDocuments(id).stream()
                .map(DocumentJson::fromOrderDocument).collect(toList());
    }

    @Authorize(operations = "order.edit")
    @PatchMapping(value = "/{id}/service-type")
    public OrderJson updateServiceType(@PathVariable Long id, @RequestBody UpdateServiceTypeRequestJson request) {
        updateOrderService.updateServiceTypeAndUnloadingAppointments(id, request);
        return OrderJson.fromEntity(listOrderService.findByIdWithShipmentsOrThrowException(id));
    }

    @Authorize(operations = "order.edit")
    @PatchMapping(value = "/{id}/truck-load-type")
    public OrderJson updateTruckLoadType(@PathVariable Long id, @RequestBody UpdateTruckLoadTypeRequestJson request) {
        updateOrderService.updateTruckLoadTypeAndUnloadingAppointments(id, request);
        return OrderJson.fromEntity(listOrderService.findByIdWithShipmentsOrThrowException(id));
    }

    @PatchMapping(value = "/{id}/documents")
    public List<DocumentJson> saveDocument(@PathVariable Long id, @RequestBody List<DocumentJson> documents) {
        orderDocumentService.saveDocument(id, documents.stream().map(DocumentJson::toOrderDocument).collect(toList()));
        return getDocuments(id);
    }

    @DeleteMapping(value = "/{id}/documents/{documentId}")
    public List<DocumentJson> deleteDocument(@PathVariable Long id, @PathVariable Long documentId) {
        orderDocumentService.deleteDocument(documentId);
        return getDocuments(id);
    }
    
    @Authorize(operations = "order.edit")
    @PatchMapping(value = "/{id}/status")
    public OrderJson updateStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequestJson request) {
        updateOrderService.updateStatus(id, request);
        return OrderJson.fromEntity(listOrderService.findByIdWithShipmentsOrThrowException(id));
    }
}
