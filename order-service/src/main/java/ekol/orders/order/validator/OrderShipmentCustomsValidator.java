package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.CodeNameEmbeddable;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;
import ekol.orders.order.service.KartoteksServiceClient;
import ekol.orders.order.service.LocationServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderShipmentCustomsValidator {

    private LocationServiceClient locationServiceClient;
    private KartoteksServiceClient kartoteksServiceClient;

    @Autowired
    public OrderShipmentCustomsValidator(LocationServiceClient locationServiceClient, KartoteksServiceClient kartoteksServiceClient){
        this.locationServiceClient = locationServiceClient;
        this.kartoteksServiceClient = kartoteksServiceClient;
    }


    public void validateDeparture(OrderShipmentDepartureCustoms customs){
        if(customs == null){
            throw new ValidationException("order shipment should have an departure customs info");
        }
        validateIdName(customs.getCustomsAgent(), "order shipment should have a departure customs agent");
        validateIdName(customs.getCustomsAgentLocation(), "order shipment should have a departure customs agent location");

        validateCustomsAgent(customs.getCustomsAgent());
        validateCustomsAgentLocation(customs.getCustomsAgentLocation());
    }
    public void validateArrival(OrderShipmentArrivalCustoms customs){
        if(customs == null){
            throw new ValidationException("order shipment should have an arrival customs info");
        }
        validateIdName(customs.getCustomsAgent(), "order shipment should have an arrival customs agent");
        validateIdName(customs.getCustomsAgentLocation(), "order shipment should have an arrival customs agent location");

        validateCustomsAgent(customs.getCustomsAgent());
        validateCustomsAgentLocation(customs.getCustomsAgentLocation());
    }
    public void validateDepartureFromTR(OrderShipmentDepartureCustoms customs){
        if(customs == null){
            throw new ValidationException("order shipment should have an departure customs info");
        }
        validateIdName(customs.getCustomsOffice(), "order shipment should have a departure customs office");
        validateIdName(customs.getCustomsAgent(), "order shipment should have a departure customs agent");

        validateCustomsOffice(customs.getCustomsOffice());
        validateCustomsAgent(customs.getCustomsAgent());

    }

    public void validateArrivalToTR(OrderShipmentArrivalCustoms customs){
        final String freeZoneType = "FREE_ZONE";
        if(customs == null){
            throw new ValidationException("order shipment should have an arrival customs info");
        }
        validateIdName(customs.getCustomsOffice(), "order shipment should have an arrival customs office");
        validateCustomsOffice(customs.getCustomsOffice());

        validateCodeName(customs.getCustomsType(), "order shipment should have an arrival customs type");
        if(!customs.getCustomsType().getCode().equals(freeZoneType)){
            validateIdName(customs.getCustomsLocation(), "order shipment should have an arrival customs location");
            validateCustomsLocation(customs.getCustomsLocation());
        }
        validateIdName(customs.getCustomsAgent(), "order shipment should have an arrival customs agent");
        validateCustomsAgent(customs.getCustomsAgent());
    }

    private void validateIdName(IdNameEmbeddable idName, String message){
        if(idName == null || idName.getId() == null || StringUtils.isBlank(idName.getName())){
            throw new ValidationException(message);
        }
    }

    private void validateCodeName(CodeNameEmbeddable codeName, String message){
        if(codeName == null || StringUtils.isBlank(codeName.getCode()) || StringUtils.isBlank(codeName.getName())){
            throw new ValidationException(message);
        }
    }

    private void validateCustomsOffice(IdNameEmbeddable customsOffice){
        if(!this.locationServiceClient.isCustomsOfficeExists(customsOffice.getId())){
            throw new ValidationException("customs office ''{0}'' does not exist", customsOffice.getName());
        }
    }
    private void validateCustomsAgent(IdNameEmbeddable customsAgent){
        if(!this.kartoteksServiceClient.isCompanyExists(customsAgent.getId())){
            throw new ValidationException("customs agent ''{0}'' does not exist", customsAgent.getName());
        }
    }

    private void validateCustomsAgentLocation(IdNameEmbeddable customsAgentLocation){
        if(!this.locationServiceClient.isCustomsLocationExists(customsAgentLocation.getId())){
            throw new ValidationException("customs agent location ''{0}'' does not exist", customsAgentLocation.getName());
        }
    }

    private void validateCustomsLocation(IdNameEmbeddable customsLocation){
        if(!this.locationServiceClient.isCustomsLocationExists(customsLocation.getId())){
            throw new ValidationException("customs location ''{0}'' does not exist", customsLocation.getName());
        }
    }
}
