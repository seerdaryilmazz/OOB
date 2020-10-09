package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.VehicleRequirement;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class VehicleRequirementValidator {

    public void validate(VehicleRequirement vehicleRequirement){
        if(vehicleRequirement.getQuote() == null || vehicleRequirement.getQuote().getId() == null){
            throw new ValidationException("Vehicle requirement should be assigned to a quote");
        }
        if(vehicleRequirement.getRequirement() == null){
            throw new ValidationException("Vehicle requirement should have a requirement type");
        }
        if(vehicleRequirement.getOperationType() == null){
            throw new ValidationException("Vehicle requirement should have a operation type");
        }
    }
}
