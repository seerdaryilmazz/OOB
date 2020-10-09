package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ValidationException;
import ekol.location.domain.location.warehouse.Warehouse;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by burak on 09/02/17.
 */
public class WarehouseValidator {

    public static void validate(Warehouse warehouse) throws ValidationException {

        if (StringUtils.isBlank(warehouse.getName())) {
            throw new BadRequestException("Warehouse Name can not be empty");
        }

        if (StringUtils.isBlank(warehouse.getLocalName())) {
            throw new BadRequestException("Warehouse Local Name can not be empty");
        }

        if (warehouse.getWarehouseOwnerType() == null) {
            throw new BadRequestException("Warehouse type does not exist.");
        }

        if (warehouse.getCompanyLocation() == null) {
            throw new BadRequestException("Warehouse's location is not selected. ");
        }

        if(!warehouse.getCompanyLocation().isValid()) {
            throw new BadRequestException("Warehouse's location Data is not valid. ");
        }

        WarehouseZoneValidator.validate(warehouse);

        WarehouseRampValidator.validate(warehouse);

        CustomsDetailsValidator.validate(warehouse.getCustomsDetails(), warehouse.getEstablishment().getCountryIso());

    }

}
