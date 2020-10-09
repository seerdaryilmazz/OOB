package ekol.orders.order.controller;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;

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

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.Appointment;
import ekol.orders.order.domain.dto.json.DocumentJson;
import ekol.orders.order.domain.dto.json.OrderJson;
import ekol.orders.order.domain.dto.json.OrderShipmentAdrJson;
import ekol.orders.order.domain.dto.json.OrderShipmentCustomsJson;
import ekol.orders.order.domain.dto.json.OrderShipmentEquipmentRequirementJson;
import ekol.orders.order.domain.dto.json.OrderShipmentJson;
import ekol.orders.order.domain.dto.json.OrderShipmentVehicleRequirementJson;
import ekol.orders.order.domain.dto.json.ShipmentPartyJson;
import ekol.orders.order.domain.dto.json.updateOrder.AmountWithCurrencyJson;
import ekol.orders.order.domain.dto.json.updateOrder.ShipmentIdMappingJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateDefinitionOfGoodsJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateHealthCertificateRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateShipmentDateRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateShipmentUnitsRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateTemperatureLimitsRequestJson;
import ekol.orders.order.service.OrderShipmentCustomsService;
import ekol.orders.order.service.OrderShipmentDateService;
import ekol.orders.order.service.OrderShipmentDocumentService;
import ekol.orders.order.service.OrderShipmentEquipmentService;
import ekol.orders.order.service.OrderShipmentIdMappingService;
import ekol.orders.order.service.OrderShipmentOrderNumbersService;
import ekol.orders.order.service.OrderShipmentVehicleSpecsService;
import ekol.orders.order.service.UpdateOrderShipmentService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/order-shipment")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class OrderShipmentController {

    private OrderShipmentOrderNumbersService orderNumbersService;
    private OrderShipmentDocumentService orderShipmentDocumentService;
    private OrderShipmentDateService orderShipmentDateService;
    private UpdateOrderShipmentService updateOrderShipmentService;
    private OrderShipmentCustomsService shipmentCustomsService;
    private OrderShipmentVehicleSpecsService shipmentVehicleSpecsService;
    private OrderShipmentEquipmentService shipmentEquipmentService;
    private OrderShipmentIdMappingService orderShipmentIdMappingService;

    @GetMapping("/{id}/sender-order-numbers")
    public Set<String> getSenderOrderNumbers(@PathVariable Long id) {
        return orderNumbersService.findSenderOrderNumbers(id);
    }

    @GetMapping("/{id}/consignee-order-numbers")
    public Set<String> getConsigneeOrderNumbers(@PathVariable Long id) {
        return orderNumbersService.findConsigneeOrderNumbers(id);
    }

    @GetMapping("/{id}/customer-order-numbers")
    public Set<String> getCustomerOrderNumbers(@PathVariable Long id) {
        return orderNumbersService.findCustomerOrderNumbers(id);
    }
    @GetMapping("/{id}/loading-order-numbers")
    public Set<String> getLoadingOrderNumbers(@PathVariable Long id) {
    	return orderNumbersService.findLoadingOrderNumbers(id);
    }
    @GetMapping("/{id}/unloading-order-numbers")
    public Set<String> getUnloadingOrderNumbers(@PathVariable Long id) {
    	return orderNumbersService.findUnloadingOrderNumbers(id);
    }

    @GetMapping("/{id}/documents")
    public List<DocumentJson> getDocuments(@PathVariable Long id) {
        return orderShipmentDocumentService.getDocuments(id).stream()
                .map(DocumentJson::fromShipmentDocument).collect(toList());
    }

    @PatchMapping("/{id}/customer-order-numbers")
    public Set<String> updateCustomerOrderNumbers(@PathVariable Long id, @RequestBody Set<String> orderNumbers) {
        return orderNumbersService.updateCustomerOrderNumbers(id, orderNumbers).getCustomerOrderNumbers();
    }

    @PatchMapping("/{id}/sender-order-numbers")
    public Set<String> updateSenderOrderNumbers(@PathVariable Long id, @RequestBody Set<String> orderNumbers) {
        return orderNumbersService.updateSenderOrderNumbers(id, orderNumbers).getSenderOrderNumbers();
    }

    @PatchMapping("/{id}/consignee-order-numbers")
    public Set<String> updateConsigneeOrderNumbers(@PathVariable Long id, @RequestBody Set<String> orderNumbers) {
        return orderNumbersService.updateConsigneeOrderNumbers(id, orderNumbers).getConsigneeOrderNumbers();
    }

    @PatchMapping("/{id}/loading-order-numbers")
    public Set<String> updateLoadingOrderNumbers(@PathVariable Long id, @RequestBody Set<String> orderNumbers) {
    	return orderNumbersService.updateLoadingOrderNumbers(id, orderNumbers).getLoadingOrderNumbers();
    }

    @PatchMapping("/{id}/unloading-order-numbers")
    public Set<String> updateUnloadingOrderNumbers(@PathVariable Long id, @RequestBody Set<String> orderNumbers) {
    	return orderNumbersService.updateUnloadingOrderNumbers(id, orderNumbers).getUnloadingOrderNumbers();
    }

    @PatchMapping("/{id}/documents")
    public List<DocumentJson> saveDocument(@PathVariable Long id, @RequestBody List<DocumentJson> documents) {
        orderShipmentDocumentService.saveDocument(id, documents.stream().map(DocumentJson::toShipmentDocument).collect(toList()));
        return getDocuments(id);
    }

    @DeleteMapping("/{id}/documents/{documentId}")
    public List<DocumentJson> deleteDocument(@PathVariable Long id, @PathVariable Long documentId) {
        orderShipmentDocumentService.deleteDocument(documentId);
        return getDocuments(id);
    }

    @PatchMapping("/{id}/ready-date")
    public OrderShipmentJson updateReadyDate(@PathVariable Long id, @RequestBody UpdateShipmentDateRequestJson request) {
        Appointment unloadingAppointment = request.getUnloadingAppointment() != null ?
                request.getUnloadingAppointment().toEntity() : null;
        return OrderShipmentJson.fromEntity(
                orderShipmentDateService.updateReadyDate(id, new FixedZoneDateTime(request.getReadyDate()), unloadingAppointment)
        );
    }

    @PatchMapping("/{id}/loading-appointment")
    public OrderShipmentJson updateLoadingAppointment(@PathVariable Long id, @RequestBody UpdateShipmentDateRequestJson request) {
        Appointment unloadingAppointment = request.getUnloadingAppointment() != null ?
                request.getUnloadingAppointment().toEntity() : null;
        return OrderShipmentJson.fromEntity(
                orderShipmentDateService.updateLoadingAppointment(id, request.getLoadingAppointment().toEntity(), unloadingAppointment));
    }

    @PatchMapping("/{id}/unloading-appointment")
    public OrderShipmentJson updateUnloadingAppointment(@PathVariable Long id, @RequestBody UpdateShipmentDateRequestJson request) {
        return OrderShipmentJson.fromEntity(
                orderShipmentDateService.updateUnloadingAppointment(id, request.getUnloadingAppointment().toEntity()));
    }

    @DeleteMapping("/{id}/unloading-appointment")
    public OrderShipmentJson deleteUnloadingAppointment(@PathVariable Long id) {
        return OrderShipmentJson.fromEntity(orderShipmentDateService.deleteUnloadingAppointment(id));
    }

    @PatchMapping("/{id}/incoterm")
    public OrderShipmentJson updateIncoterm(@PathVariable Long id, @RequestBody IdNamePair incoterm) {
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.updateIncoterm(id, incoterm));
    }

    @PatchMapping("/{id}/payment-method")
    public OrderShipmentJson updatePaymentMethod(@PathVariable Long id, @RequestBody IdNamePair paymentMethod) {
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.updatePaymentMethod(id, paymentMethod));
    }

    @PatchMapping("/{id}/set-insurance")
    public OrderShipmentJson setInsurance(@PathVariable Long id) {
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.setInsurance(id));
    }

    @PatchMapping("/{id}/remove-insurance")
    public OrderShipmentJson removeInsurance(@PathVariable Long id) {
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.removeInsurance(id));
    }

    @PatchMapping("/{id}/value-of-goods")
    public OrderShipmentJson updateValueOfGoods(@PathVariable Long id, @RequestBody AmountWithCurrencyJson request) {
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.updateValueOfGoods(id, request.toEntity()));
    }

    @PatchMapping("/{id}/shipment-units")
    public OrderShipmentJson updateShipmentUnits(@PathVariable Long id, @RequestBody UpdateShipmentUnitsRequestJson request) {
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.updateShipmentUnitsAndTotalMeasurements(id, request));
    }

    @PostMapping("/{id}/adr-details")
    public OrderShipmentJson addShipmentAdrDetails(@PathVariable Long id, @RequestBody OrderShipmentAdrJson request) {
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.addShipmentAdrDetails(id, request.toEntity()));

    }

    @PatchMapping("/{id}/adr-details/{adrDetailsId}")
    public OrderShipmentJson updateShipmentAdrDetails(@PathVariable Long id, @PathVariable Long adrDetailsId,
                                                    @RequestBody OrderShipmentAdrJson request) {
        request.setId(adrDetailsId);
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.updateShipmentAdrDetails(id, request.toEntity()));
    }

    @DeleteMapping("/{id}/adr-details/{adrDetailsId}")
    public OrderShipmentJson deleteShipmentAdrDetails(@PathVariable Long id, @PathVariable Long adrDetailsId) {
        return OrderShipmentJson.fromEntity(updateOrderShipmentService.deleteShipmentAdrDetails(id, adrDetailsId));
    }

    @PatchMapping("/{id}/departure-customs")
    public OrderShipmentJson updateDepartureCustoms(@PathVariable Long id, @RequestBody OrderShipmentCustomsJson request) {
        return OrderShipmentJson.fromEntity(shipmentCustomsService.saveDepartureCustoms(id, request.toDepartureEntity()));
    }

    @PatchMapping("/{id}/arrival-customs")
    public OrderShipmentJson updateArrivalCustoms(@PathVariable Long id, @RequestBody OrderShipmentCustomsJson request) {
        return OrderShipmentJson.fromEntity(shipmentCustomsService.saveArrivalCustoms(id, request.toArrivalEntity()));
    }

    @PatchMapping("/{id}/vehicle-requirements")
    public OrderShipmentJson updateVehicleRequirements(@PathVariable Long id, @RequestBody List<OrderShipmentVehicleRequirementJson> request) {
        return OrderShipmentJson.fromEntity(
                shipmentVehicleSpecsService.updateVehicleRequirements(id, request.stream()
                        .map(OrderShipmentVehicleRequirementJson::toEntity)
                        .collect(toList()))
        );
    }

    @PatchMapping("/{id}/equipment-requirement")
    public OrderShipmentJson updateEquipmentRequirement(@PathVariable Long id, @RequestBody OrderShipmentEquipmentRequirementJson request) {
        return OrderShipmentJson.fromEntity(
                shipmentEquipmentService.saveEquipmentRequirement(id, request.toEntity())
        );
    }
    @PostMapping("/{id}/equipment-requirement")
    public OrderShipmentJson addEquipmentRequirement(@PathVariable Long id, @RequestBody OrderShipmentEquipmentRequirementJson request) {
        return OrderShipmentJson.fromEntity(
                shipmentEquipmentService.saveEquipmentRequirement(id, request.toEntity())
        );
    }
    @DeleteMapping("/{id}/equipment-requirement/{requirementId}")
    public OrderShipmentJson deleteEquipmentRequirement(@PathVariable Long id, @PathVariable Long requirementId) {
        return OrderShipmentJson.fromEntity(
                shipmentEquipmentService.deleteEquipmentRequirement(id, requirementId)
        );
    }

    @PatchMapping("/{id}/health-certificates")
    public OrderShipmentJson saveHealthCertificates(@PathVariable Long id, @RequestBody UpdateHealthCertificateRequestJson request) {
        return OrderShipmentJson.fromEntity(
                updateOrderShipmentService.updateHealthCertificates(id, request)
        );
    }

    @PatchMapping("/{id}/temperature-limits")
    public OrderShipmentJson saveTemperatureLimits(@PathVariable Long id, @RequestBody UpdateTemperatureLimitsRequestJson request) {
        return OrderShipmentJson.fromEntity(
                updateOrderShipmentService.updateTemperatureLimits(id, request)
        );
    }

    @PatchMapping("/{id}/definition-of-goods")
    public OrderShipmentJson saveDefinitionOfGoods(@PathVariable Long id, @RequestBody UpdateDefinitionOfGoodsJson request) {
    	return OrderShipmentJson.fromEntity(updateOrderShipmentService.updateDefinitionOfGoods(id, request));
    }
    
    @PatchMapping("/id-mapping")
    public OrderJson saveIdMapping(@RequestParam String code, @RequestBody ShipmentIdMappingJson request) {
    	return OrderJson.fromEntity(orderShipmentIdMappingService.updateApplicationIdMapping(code, request).getOrder());
    }
    
    @PatchMapping("/{id}/manufacturer")
    public OrderShipmentJson saveManufacturer(@PathVariable Long id, @RequestBody ShipmentPartyJson request) {
    	return OrderShipmentJson.fromEntity(updateOrderShipmentService.updateManufacturer(id, request));
    }
    
    @DeleteMapping("/{id}/manufacturer")
    public OrderShipmentJson deleteManufacturer(@PathVariable Long id) {
    	return OrderShipmentJson.fromEntity(updateOrderShipmentService.deleteManufacturer(id));
    }
}
