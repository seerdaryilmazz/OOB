package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentVehicleRequirement;
import org.springframework.stereotype.Component;

@Component
public class OrderShipmentVehicleValidator {

    public void validate(OrderShipment shipment){
        shipment.getVehicleRequirements().forEach(this::validate);
    }
    public void validate(OrderShipmentVehicleRequirement requirement){
        if(requirement.getRequirement() == null){
            throw new ValidationException("order shipment vehicle requirement should have vehicle feature");
        }
        if(requirement.getOperationType() == null){
            throw new ValidationException("order shipment vehicle requirement should have operation type");
        }
    }
}
