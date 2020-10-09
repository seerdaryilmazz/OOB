package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderDocument;
import ekol.orders.order.domain.Status;
import ekol.orders.order.service.KartoteksServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;

@Component
public class OrderValidator {

    private DocumentValidator documentValidator;
    private KartoteksServiceClient kartoteksServiceClient;
    private OrderShipmentValidator orderShipmentValidator;

    @Autowired
    public OrderValidator(KartoteksServiceClient kartoteksServiceClient,
                          OrderShipmentValidator orderShipmentValidator,
                          DocumentValidator documentValidator){

        this.kartoteksServiceClient = kartoteksServiceClient;
        this.orderShipmentValidator = orderShipmentValidator;
        this.documentValidator = documentValidator;
    }

    public void validateNew(Order order){
        if (order.getId() != null) {
            throw new ValidationException("New order should not have an ID");
        }
        validateSubsidiary(order.getSubsidiary());
        validateCustomer(order.getCustomer());
        validateOriginalCustomer(order.getOriginalCustomer());

        if(order.getServiceType() == null){
            throw new ValidationException("Order should have a service type");
        }
        if(order.getTruckLoadType() == null){
            throw new ValidationException("Order should have a truck load type");
        }
        if(order.getStatus() == null){
            throw new ValidationException("Order should have a status");
        }
        if(!Arrays.asList(Status.CREATED, Status.CONFIRMED).contains(order.getStatus())){
            throw new ValidationException("New order should not have {0} status", order.getStatus());
        }
        if(order.getShipments().isEmpty()){
            throw new ValidationException("Order should have at least one shipment");
        }

        order.getShipments().forEach(orderShipmentValidator::validate);
    }

    public void validateMeasurements(Order order){
        order.getShipments().forEach(orderShipmentValidator::validateTotalMeasurements);
    }

    public void validateLoadSpecs(Order order){
        order.getShipments().forEach(orderShipmentValidator::validateLoadSpecs);
    }

    public void validateCustoms(Order order){
        order.getShipments().forEach(orderShipmentValidator::validateCustoms);
    }

    private void validateSubsidiary(IdNameEmbeddable subsidiary){
        //TODO: check subsidiary is valid for user
        if(subsidiary == null || subsidiary.getId() == null || StringUtils.isBlank(subsidiary.getName())){
            throw new ValidationException("Order should have a subsidiary");
        }
    }

    private void validateCustomer(IdNameEmbeddable customer){
        if(customer == null || customer.getId() == null || StringUtils.isBlank(customer.getName())){
            throw new ValidationException("Order should have a customer");
        }
        if(!kartoteksServiceClient.isCompanyExists(customer.getId())){
            throw new ValidationException("Customer with id {0} does not exist", customer.getId().toString());
        }
    }

    private void validateOriginalCustomer(IdNameEmbeddable originalCustomer){
        if(originalCustomer != null && originalCustomer.getId() != null){
            if(!kartoteksServiceClient.isCompanyExists(originalCustomer.getId())){
                throw new ValidationException("Original customer with id {0} does not exist", originalCustomer.getId().toString());
            }
        }
    }

    public void validateDocuments(Order order){
        documentValidator.validate(order.getDocuments().stream().map(OrderDocument::getDocument).collect(toList()));
        order.getShipments().forEach(orderShipmentValidator::validateDocuments);
    }



}
