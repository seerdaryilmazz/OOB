package ekol.orders.transportOrder.domain.validator;


import ekol.orders.transportOrder.domain.TransportOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by burak on 06/10/17.
 */
@Component
public class TransportOrderValidator {

    @Autowired
    private ekol.orders.transportOrder.domain.validator.ShipmentValidator shipmentValidator;

    @Autowired
    private ekol.orders.transportOrder.domain.validator.VehicleRequirementValidator vehicleRequirementValidator;

    public void validate(TransportOrder order) {

        if(order.getShipments() != null) {
            order.getShipments().stream().forEach( shipment -> {
                shipmentValidator.validate(shipment);
            });
        }

        vehicleRequirementValidator.validate(order);

    }
}
