package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.lookup.domain.*;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.lookup.repository.IncotermRepository;
import ekol.orders.lookup.repository.PaymentMethodRepository;
import ekol.orders.order.domain.AmountWithCurrency;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentDocument;
import ekol.orders.order.domain.OrderShipmentUnit;
import ekol.orders.order.domain.dto.response.location.CountryResponse;
import ekol.orders.order.service.LocationServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderShipmentValidator {

    private OrderShipmentDateValidator orderShipmentDateValidator;
    private OrderHandlingPartyValidator orderHandlingPartyValidator;
    private OrderShipmentUnitValidator orderShipmentUnitValidator;
    private OrderShipmentCustomsValidator orderShipmentCustomsValidator;
    private OrderShipmentAdrValidator orderShipmentAdrValidator;
    private OrderShipmentVehicleValidator orderShipmentVehicleValidator;
    private OrderShipmentEquipmentValidator orderShipmentEquipmentValidator;
    private DocumentValidator documentValidator;
    private IncotermRepository incotermRepository;
    private PaymentMethodRepository paymentMethodRepository;
    private DocumentTypeRepository documentTypeRepository;
    private LocationServiceClient locationServiceClient;

    @Autowired
    public OrderShipmentValidator(OrderHandlingPartyValidator orderHandlingPartyValidator,
                                  OrderShipmentDateValidator orderShipmentDateValidator,
                                  OrderShipmentUnitValidator orderShipmentUnitValidator,
                                  OrderShipmentCustomsValidator orderShipmentCustomsValidator,
                                  OrderShipmentAdrValidator orderShipmentAdrValidator,
                                  OrderShipmentVehicleValidator orderShipmentVehicleValidator,
                                  OrderShipmentEquipmentValidator orderShipmentEquipmentValidator,
                                  DocumentValidator documentValidator,
                                  IncotermRepository incotermRepository,
                                  PaymentMethodRepository paymentMethodRepository,
                                  DocumentTypeRepository documentTypeRepository,
                                  LocationServiceClient locationServiceClient){

        this.orderHandlingPartyValidator = orderHandlingPartyValidator;
        this.orderShipmentDateValidator = orderShipmentDateValidator;
        this.orderShipmentUnitValidator = orderShipmentUnitValidator;
        this.orderShipmentCustomsValidator = orderShipmentCustomsValidator;
        this.orderShipmentVehicleValidator = orderShipmentVehicleValidator;
        this.orderShipmentEquipmentValidator = orderShipmentEquipmentValidator;
        this.documentValidator = documentValidator;
        this.incotermRepository = incotermRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.orderShipmentAdrValidator = orderShipmentAdrValidator;
        this.documentTypeRepository = documentTypeRepository;
        this.locationServiceClient = locationServiceClient;
    }

    public void validate(OrderShipment shipment){
        if (shipment.getId() != null) {
            throw new ValidationException("New order shipment should not have an ID");
        }
        validateIncoterm(shipment.getIncoterm());
        validatePaymentMethod(shipment.getPaymentMethod());

        orderShipmentDateValidator.validate(shipment);
        orderHandlingPartyValidator.validateSender(shipment.getSender());
        orderHandlingPartyValidator.validateConsignee(shipment.getConsignee());

        if(shipment.getSender().getHandlingLocation().getId().equals(shipment.getConsignee().getHandlingLocation().getId())){
            throw new ValidationException("Order shipment sender and consignee handling should not have the same location");
        }

        validateValueOfGoods(shipment.getValueOfGoods());
        validateAdrDetails(shipment);
        validateTemperatureLimits(shipment.getTemperatureMinValue(), shipment.getTemperatureMaxValue());
        validateHealthCertificates(shipment);
        orderShipmentVehicleValidator.validate(shipment);
        orderShipmentEquipmentValidator.validate(shipment);
        validateOrderNumbers(shipment.getCustomerOrderNumbers());
        validateOrderNumbers(shipment.getSenderOrderNumbers());
        validateOrderNumbers(shipment.getConsigneeOrderNumbers());

        shipment.getUnits().forEach(orderShipmentUnitValidator::validate);
    }
    public void validateDocuments(OrderShipment shipment){
        documentValidator.validate(shipment.getDocuments().stream().map(OrderShipmentDocument::getDocument).collect(Collectors.toList()));
    }
    public void validateIncoterm(Incoterm incoterm){
        if(incoterm == null || incoterm.getId() == null){
            throw new ValidationException("Order shipment should have an incoterm");
        }
        if(!incotermRepository.findById(incoterm.getId()).isPresent()){
            throw new ValidationException("Incoterm with id {0} does not exist", incoterm.getId().toString());
        }
    }
    public void validatePaymentMethod(PaymentMethod paymentMethod){
        if(paymentMethod == null || paymentMethod.getId() == null){
            throw new ValidationException("Order shipment should have a payment method");
        }
        if(!paymentMethodRepository.findById(paymentMethod.getId()).isPresent()){
            throw new ValidationException("Payment method with id {0} does not exist", paymentMethod.getId().toString());
        }
    }

    public void validateValueOfGoods(AmountWithCurrency amountWithCurrency){
        if(amountWithCurrency != null &&
                (amountWithCurrency.getAmount() != null ^ StringUtils.isNotBlank(amountWithCurrency.getCurrency()))){
            throw new ValidationException("Order shipment value of goods should have amount and currency");
        }
    }

    private void validateAdrDetails(OrderShipment shipment){
        shipment.getAdrDetails().forEach(orderShipmentAdr ->
            orderShipmentAdrValidator.validate(orderShipmentAdr)
        );
    }

    public void validateTemperatureLimits(Integer minValue, Integer maxValue){
        final int possibleMinValue = -25;
        final int possibleMaxValue = 25;
        if(minValue != null && maxValue != null && minValue > maxValue){
            throw new ValidationException("order shipment should have temperature max. value higher than temperature min. value");
        }
        if(minValue != null && minValue < possibleMinValue){
            throw new ValidationException("order shipment should have temperature min. value higher than {0}", possibleMinValue);
        }
        if(maxValue != null && maxValue > possibleMaxValue){
            throw new ValidationException("order shipment should have temperature max. value lower than {0}", possibleMaxValue);
        }
    }

    private void validateHealthCertificates(OrderShipment shipment){
        shipment.getHealthCertificateTypes().forEach(certificate -> {
            if(certificate == null || certificate.getId() == null){
                throw new ValidationException("order shipment health certificate should have document type");
            }
            Optional<DocumentType> documentType = documentTypeRepository.findById(certificate.getId());
            if(!documentType.isPresent()){
                throw new ValidationException("Document type with id {0} does not exist", certificate.getId().toString());
            }
            if(!documentType.get().getDocumentGroup().equals(DocumentTypeGroup.HEALTH_CERTIFICATE)){
                throw new ValidationException("order shipment health certificate should have document type with group ''Health Certificate''");
            }
        });

        if(shipment.getBorderCustoms() != null && shipment.getBorderCustoms().getId() != null){
            if(!locationServiceClient.isCustomsOfficeExists(shipment.getBorderCustoms().getId())){
                throw new ValidationException("Border crossing with id {0} does not exist", shipment.getBorderCustoms().getId());
            }
        }
    }

    public void validateTotalMeasurements(OrderShipment shipment){
        if(shipment.getGrossWeight() == null && shipment.getTotalVolume() == null && shipment.getTotalLdm() == null){
            throw new ValidationException("order shipment should have a one of gross weight, total ldm or total volume");
        }
        if(shipment.getGrossWeight() != null && shipment.getNetWeight() != null &&
                shipment.getGrossWeight().compareTo(shipment.getNetWeight()) < 0){
            throw new ValidationException("order shipment should have a gross weight greater than net weight");
        }
        
        Set<PackageType> unitPackageTypes = shipment.getUnits().stream()
                .map(t->t.getPackageType())
                .collect(Collectors.toSet());

        boolean unitPackageTypeMatch =
        		unitPackageTypes.parallelStream().map(PackageType::getId)
                        .allMatch(unitTypeId ->
                                shipment.getPackageTypes().parallelStream()
                                        .map(PackageType::getId)
                                        .anyMatch(shipmentTypeId -> shipmentTypeId.equals(unitTypeId)));
        if(!unitPackageTypeMatch){
    		throw new ValidationException("order shipment should have package groups according to shipment unit package types");
    	}
        
        if(shipment.getTotalQuantity() != null){
            Integer totalQuantity = shipment.getUnits().stream()
                    .filter(shipmentUnit -> shipmentUnit.getQuantity() != null)
                    .map(OrderShipmentUnit::getQuantity).reduce(0, (arg1, arg2) -> arg1 + arg2);

            if(shipment.getTotalQuantity() < totalQuantity){
                throw new ValidationException("order shipment total quantity should be greater than {0}",
                        totalQuantity.toString());
            }
        }

        if(shipment.getTotalVolume() != null){
            BigDecimal totalVolume = shipment.getUnits().stream()
                    .filter(shipmentUnit -> shipmentUnit.getVolume() != null)
                    .map(OrderShipmentUnit::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);

            if(totalVolume.compareTo(shipment.getTotalVolume()) > 0){
                throw new ValidationException("order shipment total volume should be greater than {0}",
                        totalVolume.setScale(2, RoundingMode.HALF_EVEN).toString());
            }
        }

        if(shipment.getTotalLdm() != null){
            BigDecimal totalLdm = shipment.getUnits().stream()
                    .filter(shipmentUnit -> shipmentUnit.getLdm() != null)
                    .map(OrderShipmentUnit::getLdm).reduce(BigDecimal.ZERO, BigDecimal::add);

            if(totalLdm.compareTo(shipment.getTotalLdm()) > 0){
                throw new ValidationException("order shipment total ldm should be greater than {0}",
                        totalLdm.setScale(2, RoundingMode.HALF_EVEN).toString());
            }
        }
    }

    public void validateLoadSpecs(OrderShipment orderShipment){
        if (orderShipment.isHangingLoad() && orderShipment.isLongLoad()) {
            throw new ValidationException("Order shipment should not have hanging loads and long loads together");
        }
    }

    public void validateCustoms(OrderShipment orderShipment){
        CountryResponse loadingCountry = this.locationServiceClient.getCountryDetails(orderShipment.getSender().getHandlingLocationCountryCode());
        CountryResponse unloadingCountry = this.locationServiceClient.getCountryDetails(orderShipment.getConsignee().getHandlingLocationCountryCode());
        if(!loadingCountry.getIso().equals(unloadingCountry.getIso()) && !(loadingCountry.isEuMember() && unloadingCountry.isEuMember())){
            if(loadingCountry.isTurkey()){
                orderShipmentCustomsValidator.validateDepartureFromTR(orderShipment.getDepartureCustoms());
            }else if(orderShipment.getDepartureCustoms() != null){
                orderShipmentCustomsValidator.validateDeparture(orderShipment.getDepartureCustoms());
            }
            if(unloadingCountry.isTurkey()){
                orderShipmentCustomsValidator.validateArrivalToTR(orderShipment.getArrivalCustoms());
            }else{
                orderShipmentCustomsValidator.validateArrival(orderShipment.getArrivalCustoms());
            }
        }
    }

    public void validateOrderNumbers(Set<String> orderNumbers){
        if(orderNumbers.stream().anyMatch(StringUtils::isBlank)){
            throw new ValidationException("Order number should not be empty");
        }
    }
}
