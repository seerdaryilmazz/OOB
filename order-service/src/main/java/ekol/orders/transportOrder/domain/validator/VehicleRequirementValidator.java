package ekol.orders.transportOrder.domain.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.lookup.domain.PermissionType;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.domain.VehicleFeature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 06/10/17.
 */
@Component
public class VehicleRequirementValidator {

    public void validate(TransportOrder order) {

        List<VehicleFeature> required = new ArrayList<>();
        List<VehicleFeature> notAllowed = new ArrayList<>();

        order.getVehicleRequirements().forEach(vehicleRequirement -> {
            if (vehicleRequirement.getPermissionType() == PermissionType.REQUIRED) {
                required.addAll(vehicleRequirement.getVehicleFeatures());
            } else if (vehicleRequirement.getPermissionType() == PermissionType.NOT_ALLOWED) {
                notAllowed.addAll(vehicleRequirement.getVehicleFeatures());
            } else {
                throw new ValidationException("Permission Type can not be null.");
            }
        });

        VehicleFeature.validateCooccurence(required, notAllowed);
    }

}
