package ekol.orders.order.service;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.order.domain.AmountWithCurrency;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentAdr;
import ekol.orders.order.domain.OrderShipmentDefinitionOfGoods;
import ekol.orders.order.domain.OrderShipmentUnit;
import ekol.orders.order.domain.dto.json.OrderShipmentUnitJson;
import ekol.orders.order.domain.dto.json.ShipmentPartyJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateDefinitionOfGoodsJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateHealthCertificateRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateShipmentUnitsRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateTemperatureLimitsRequestJson;
import ekol.orders.order.domain.dto.response.ShipmentLoadSpecRuleResponse;
import ekol.orders.order.repository.OrderShipmentRepository;
import ekol.orders.order.repository.OrderShipmentUnitRepository;
import ekol.orders.order.repository.OrderShipmentVehicleRequirementRepository;
import ekol.orders.order.validator.OrderShipmentAdrValidator;
import ekol.orders.order.validator.OrderShipmentUnitValidator;
import ekol.orders.order.validator.OrderShipmentValidator;
import ekol.orders.order.validator.OrderShipmentVehicleValidator;
import ekol.orders.search.service.OrderIndexService;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class UpdateOrderShipmentService {

    private ListOrderShipmentService listOrderShipmentService;
    private OrderShipmentLoadSpecsService shipmentLoadSpecsService;
    private OrderShipmentVehicleSpecsService shipmentVehicleSpecsService;
    private OrderShipmentAdrService orderShipmentAdrService;
    private OrderShipmentDocumentService orderShipmentDocumentService;
    private OrderShipmentRepository shipmentRepository;
    private OrderShipmentUnitRepository orderShipmentUnitRepository;
    private OrderShipmentVehicleRequirementRepository orderShipmentVehicleRequirementRepository;
    private OrderTemplateServiceClient orderTemplateServiceClient;
    private OrderShipmentValidator orderShipmentValidator;
    private OrderShipmentUnitValidator orderShipmentUnitValidator;
    private OrderShipmentAdrValidator orderShipmentAdrValidator;
    private OrderShipmentVehicleValidator orderShipmentVehicleValidator;
    private OrderIndexService orderIndexService;
    private UpdateDefinitionOfGoodsService updateDefinitionOfGoodsService;

    public OrderShipment updateIncoterm(Long shipmentId, IdNamePair incotermIdName){
        OrderShipment orderShipment = listOrderShipmentService.getOrThrowException(shipmentId);
        Incoterm incoterm = Incoterm.with(incotermIdName);
        orderShipment.setIncoterm(incoterm);
        orderShipmentValidator.validateIncoterm(orderShipment.getIncoterm());
        return shipmentRepository.save(orderShipment);
    }

    public OrderShipment updatePaymentMethod(Long shipmentId, IdNamePair paymentMethodIdName){
        OrderShipment orderShipment = listOrderShipmentService.getOrThrowException(shipmentId);
        PaymentMethod paymentMethod = PaymentMethod.with(paymentMethodIdName);
        orderShipment.setPaymentMethod(paymentMethod);
        orderShipmentValidator.validatePaymentMethod(orderShipment.getPaymentMethod());
        return shipmentRepository.save(orderShipment);
    }

    public OrderShipment setInsurance(Long shipmentId){
        OrderShipment orderShipment = listOrderShipmentService.getOrThrowException(shipmentId);
        orderShipment.setInsured(true);
        return shipmentRepository.save(orderShipment);
    }

    public OrderShipment removeInsurance(Long shipmentId){
        OrderShipment orderShipment = listOrderShipmentService.getOrThrowException(shipmentId);
        orderShipment.setInsured(false);
        return shipmentRepository.save(orderShipment);
    }

    public OrderShipment updateValueOfGoods(Long shipmentId, AmountWithCurrency amountWithCurrency){
        OrderShipment orderShipment = listOrderShipmentService.getOrThrowException(shipmentId);
        orderShipment.setValueOfGoods(amountWithCurrency);
        orderShipmentValidator.validateValueOfGoods(orderShipment.getValueOfGoods());
        setValuableGoodsForShipment(orderShipment);
        OrderShipment savedShipment = shipmentRepository.save(orderShipment);
        orderIndexService.indexShipment(shipmentId);
        return savedShipment;
    }

    private void setValuableGoodsForShipment(OrderShipment shipment) {
        if(shipment.getGrossWeight() != null && shipment.getTotalLdm() != null &&
                shipment.getValueOfGoods() != null && shipment.getValueOfGoods().getAmount() != null){
            ShipmentLoadSpecRuleResponse specRuleResponse =
                    orderTemplateServiceClient.runLoadSpecRulesForShipment(
                            shipment.getGrossWeight(), shipment.getTotalLdm(), shipment.getValueOfGoods().getAmount());
            shipment.setValuableLoad(specRuleResponse.isValuableLoad());
        }
    }

    public OrderShipment updateShipmentUnitsAndTotalMeasurements(Long shipmentId, UpdateShipmentUnitsRequestJson request){
        OrderShipment orderShipment = listOrderShipmentService.getOrThrowException(shipmentId);
        List<OrderShipmentUnit> newShipmentUnits = request.getUnits().stream().map(OrderShipmentUnitJson::toEntity).collect(toList());

        deleteShipmentUnitsOfShipment(orderShipment, newShipmentUnits);
        updateShipmentUnitsOfShipment(orderShipment, newShipmentUnits);
        addNewShipmentUnitsToShipment(orderShipment, newShipmentUnits);

        updateTotalMeasurementsOfShipment(orderShipment, request);

        orderShipment.getUnits()
                .forEach(orderShipmentUnit -> orderShipmentUnitValidator.validate(orderShipmentUnit));

        orderShipmentValidator.validateTotalMeasurements(orderShipment);

        shipmentLoadSpecsService.setLoadSpecsForShipment(orderShipment);
        orderShipmentValidator.validateLoadSpecs(orderShipment);

        orderShipment.setHangingLoad(
                orderShipment.getUnits().stream().anyMatch(orderShipmentUnit ->
                        orderShipmentUnit.getPackageType().isHangingPackageType()
                )
        );
        shipmentVehicleSpecsService.updateVehicleSpecsForShipment(orderShipment);
        orderShipmentVehicleValidator.validate(orderShipment);

        orderShipment.getUnits().stream()
                .filter(orderShipmentUnit -> orderShipmentUnit.getId() == null)
                .forEach(orderShipmentUnit -> orderShipmentUnit.setShipment(orderShipment));

        orderShipment.getUnits().forEach(orderShipmentUnitRepository::save);
        orderShipment.getVehicleRequirements().forEach(orderShipmentVehicleRequirementRepository::save);
        orderShipment.setVehicleRequirements(
                orderShipment.getVehicleRequirements().stream().filter(requirement -> !requirement.isDeleted()).collect(toList())
        );

        OrderShipment savedShipment = shipmentRepository.save(orderShipment);
        orderIndexService.indexShipment(shipmentId);
        return savedShipment;
    }

    private void updateTotalMeasurementsOfShipment(OrderShipment orderShipment, UpdateShipmentUnitsRequestJson request){
        orderShipment.setTotalQuantity(request.getTotalQuantity());
        orderShipment.setTotalLdm(request.getTotalLdm());
        orderShipment.setTotalVolume(request.getTotalVolume());
        orderShipment.setPackageTypes(request.getPackageTypes().stream().map(PackageType::with).collect(toSet()));
        orderShipment.setGrossWeight(request.getGrossWeight());
        orderShipment.setNetWeight(request.getNetWeight());
    }

    private void updateShipmentUnitsOfShipment(OrderShipment orderShipment, List<OrderShipmentUnit> newShipmentUnits){
        orderShipment.getUnits()
                .forEach(orderShipmentUnit -> {
                    newShipmentUnits.stream()
                            .filter(updatedOrderShipmentUnit -> orderShipmentUnit.getId().equals(updatedOrderShipmentUnit.getId()))
                            .findFirst()
                            .ifPresent(updatedOrderShipmentUnit -> updateOrderShipmentUnitValues(orderShipmentUnit, updatedOrderShipmentUnit));
                });
    }
    private void deleteShipmentUnitsOfShipment(OrderShipment orderShipment, List<OrderShipmentUnit> newShipmentUnits){
        orderShipment.getUnits().stream()
                .filter(orderShipmentUnit -> !isShipmentUnitExistsInList(orderShipmentUnit, newShipmentUnits))
                .forEach(orderShipmentUnit -> {
                    orderShipmentUnit.setDeleted(true);
                    orderShipmentUnitRepository.save(orderShipmentUnit);
                });
        orderShipment.setUnits(
                orderShipment.getUnits().stream().filter(orderShipmentUnit -> !orderShipmentUnit.isDeleted()).collect(toList())
        );
    }

    private void addNewShipmentUnitsToShipment(OrderShipment orderShipment, List<OrderShipmentUnit> newShipmentUnits){
        newShipmentUnits.stream()
                .filter(orderShipmentUnit -> orderShipmentUnit.getId() == null)
                .forEach(orderShipmentUnit -> orderShipment.getUnits().add(orderShipmentUnit));
    }

    private boolean isShipmentUnitExistsInList(OrderShipmentUnit orderShipmentUnit, List<OrderShipmentUnit> newShipmentUnits){
        return newShipmentUnits.stream()
                .anyMatch(updatedOrderShipmentUnit -> orderShipmentUnit.getId().equals(updatedOrderShipmentUnit.getId()));
    }
    private void updateOrderShipmentUnitValues(OrderShipmentUnit orderShipmentUnit, OrderShipmentUnit updatedOrderShipmentUnit){
        orderShipmentUnit.setWidth(updatedOrderShipmentUnit.getWidth());
        orderShipmentUnit.setLength(updatedOrderShipmentUnit.getLength());
        orderShipmentUnit.setHeight(updatedOrderShipmentUnit.getHeight());
        orderShipmentUnit.setPackageType(updatedOrderShipmentUnit.getPackageType());
        orderShipmentUnit.setQuantity(updatedOrderShipmentUnit.getQuantity());
        orderShipmentUnit.setStackSize(updatedOrderShipmentUnit.getStackSize());
    }

    public OrderShipment addShipmentAdrDetails(Long shipmentId, OrderShipmentAdr shipmentAdr){
        OrderShipment shipment = listOrderShipmentService.getOrThrowException(shipmentId);
        shipmentAdr.setShipment(shipment);
        orderShipmentAdrValidator.validate(shipmentAdr);

        orderShipmentAdrService.save(shipmentAdr);
        orderIndexService.indexShipment(shipmentId);
        return shipment;
    }
    public OrderShipment updateShipmentAdrDetails(Long shipmentId, OrderShipmentAdr shipmentAdrDetails){
        orderShipmentAdrService.findByIdOrThrowException(shipmentAdrDetails.getId());
        OrderShipment shipment = listOrderShipmentService.getOrThrowException(shipmentId);
        int indexOfUpdatedAdrDetails = shipment.getAdrDetails().stream()
                .map(OrderShipmentAdr::getId)
                .collect(toList())
                .indexOf(shipmentAdrDetails.getId());

        if(indexOfUpdatedAdrDetails == -1){
            throw new ValidationException("This ADR details does not belong to shipment");
        }
        shipmentAdrDetails.setShipment(shipment);
        orderShipmentAdrValidator.validate(shipmentAdrDetails);

        OrderShipmentAdr savedShipmentAdr = orderShipmentAdrService.save(shipmentAdrDetails);
        shipment.getAdrDetails().set(indexOfUpdatedAdrDetails, savedShipmentAdr);
        return shipment;
    }

    public OrderShipment deleteShipmentAdrDetails(Long shipmentId, Long shipmentAdrDetailsId){
        OrderShipment shipment = listOrderShipmentService.getOrThrowException(shipmentId);
        int indexOfUpdatedAdrDetails = shipment.getAdrDetails().stream()
                .map(OrderShipmentAdr::getId)
                .collect(toList())
                .indexOf(shipmentAdrDetailsId);

        if(indexOfUpdatedAdrDetails == -1){
            throw new ValidationException("This ADR details does not belong to shipment");
        }

        OrderShipmentAdr shipmentAdr = orderShipmentAdrService.findByIdOrThrowException(shipmentAdrDetailsId);
        shipmentAdr.setDeleted(true);
        orderShipmentAdrService.save(shipmentAdr);

        shipment.getAdrDetails().remove(indexOfUpdatedAdrDetails);
        orderIndexService.indexShipment(shipmentId);
        return shipment;
    }

    public OrderShipment updateHealthCertificates(Long shipmentId, UpdateHealthCertificateRequestJson request) {
        OrderShipment shipment = listOrderShipmentService.getOrThrowException(shipmentId);
        orderShipmentDocumentService.getDocuments(shipmentId, DocumentTypeGroup.HEALTH_CERTIFICATE)
                .forEach(document -> {
                    if(request.getHealthCertificateTypes().stream()
                            .noneMatch(certificate -> certificate.getId().equals(document.getDocument().getType().getId()))){
                        throw new ValidationException("Please select {0} certificate type",document.getDocument().getType().getName());
                    }
                });

        shipment.setBorderCrossingHealthCheck(request.getBorderCrossingHealthCheck());
        if(request.getBorderCrossingHealthCheck() != null && request.getBorderCrossingHealthCheck() && request.getBorderCustoms() != null){
            shipment.setBorderCustoms(IdNameEmbeddable.with(request.getBorderCustoms()));
        }else{
            shipment.setBorderCustoms(null);
        }
        shipment.setHealthCertificateTypes(request.getHealthCertificateTypes().stream().map(DocumentType::with).collect(toSet()));
        return shipmentRepository.save(shipment);
    }

    public OrderShipment updateTemperatureLimits(Long shipmentId, UpdateTemperatureLimitsRequestJson request) {
        OrderShipment shipment = listOrderShipmentService.getOrThrowException(shipmentId);
        orderShipmentValidator.validateTemperatureLimits(request.getMinValue(), request.getMaxValue());
        shipment.setTemperatureMinValue(request.getMinValue());
        shipment.setTemperatureMaxValue(request.getMaxValue());
        shipmentVehicleSpecsService.updateFrigoSpecsIfNecessary(shipment);
        shipment.getVehicleRequirements().forEach(orderShipmentVehicleRequirementRepository::save);
        shipment.setVehicleRequirements(
                shipment.getVehicleRequirements().stream().filter(requirement -> !requirement.isDeleted()).collect(toList())
        );
        return shipmentRepository.save(shipment);
    }
    
    public OrderShipment updateDefinitionOfGoods(Long shipmentId, UpdateDefinitionOfGoodsJson request) {
    	OrderShipment shipment = listOrderShipmentService.getOrThrowException(shipmentId);
    	List<OrderShipmentDefinitionOfGoods> newDefinitionOfGoods = request.getDefinitionOfGoods().parallelStream().map(OrderShipmentDefinitionOfGoods::with).collect(toList());
    	updateDefinitionOfGoodsService.updateDefinitionOfGoods(shipment, newDefinitionOfGoods);
    	return shipmentRepository.save(shipment);
    }

	public OrderShipment updateManufacturer(Long shipmentId, ShipmentPartyJson request) {
		OrderShipment shipment = listOrderShipmentService.getOrThrowException(shipmentId);
		shipment.setManufacturer(Optional.ofNullable(request).map(ShipmentPartyJson::toEntity).orElse(null));
		return shipmentRepository.save(shipment);
	}

	public OrderShipment deleteManufacturer(Long shipmentId) {
		return updateManufacturer(shipmentId, null);
	}
}
