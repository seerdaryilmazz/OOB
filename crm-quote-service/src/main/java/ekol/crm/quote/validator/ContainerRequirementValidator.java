package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.ContainerRequirement;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ContainerRequirementValidator {

    public void validate(ContainerRequirement containerRequirement) {
        if (containerRequirement.getQuote() == null || containerRequirement.getQuote().getId() == null) {
            throw new ValidationException("Container requirement should be assigned to a quote");
        }
        if (containerRequirement.getVolume() == null) {
            throw new ValidationException("Container requirement should have a volume");
        }
        if (containerRequirement.getType() == null) {
            throw new ValidationException("Container requirement should have a type");
        }
        if (containerRequirement.getQuantity() == null) {
            throw new ValidationException("Container requirement should have a quantity");

        }
    }
}
