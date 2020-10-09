package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.BigDecimalRange;
import ekol.orders.order.domain.OrderShipmentUnit;
import ekol.orders.transportOrder.domain.PackageTypeRestriction;
import ekol.orders.transportOrder.repository.PackageTypeRestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderShipmentUnitValidator {

    private PackageTypeRestrictionRepository packageTypeRestrictionRepository;

    @Autowired
    public OrderShipmentUnitValidator(PackageTypeRestrictionRepository packageTypeRestrictionRepository){
        this.packageTypeRestrictionRepository = packageTypeRestrictionRepository;
    }
    public void validateNew(OrderShipmentUnit shipmentUnit) {
        if (shipmentUnit.getId() != null) {
            throw new ValidationException("New order shipment unit should not have an ID");
        }
        validate(shipmentUnit);
    }

    public void validate(OrderShipmentUnit shipmentUnit){
        if(shipmentUnit.getQuantity() == null || shipmentUnit.getQuantity() <= 0){
            throw new ValidationException("order shipment unit should have a quantity");
        }
        if(shipmentUnit.getPackageType() == null || shipmentUnit.getPackageType().getId() == null){
            throw new ValidationException("order shipment unit should have a package type");
        }
        if(shipmentUnit.getWidth() != null && shipmentUnit.getLength() != null &&
                shipmentUnit.getWidth().compareTo(shipmentUnit.getLength()) > 0){
            throw new ValidationException("order shipment unit should have a width smaller than or equal to length");
        }
        Optional<PackageTypeRestriction> restrictions =
                packageTypeRestrictionRepository.findByPackageTypeId(shipmentUnit.getPackageType().getId());
        if(restrictions.isPresent()){
            BigDecimalRange widthRestrictions = restrictions.get().getWidthRangeInCentimeters();
            if(widthRestrictions != null && shipmentUnit.getWidth() != null){
                if(widthRestrictions.check(shipmentUnit.getWidth()) != 0){
                    throw new ValidationException("order shipment unit should have a width in range {0}, {1}",
                            widthRestrictions.getMinValue().toString(), widthRestrictions.getMaxValue().toString());
                }
            }
            BigDecimalRange lengthRestrictions = restrictions.get().getLengthRangeInCentimeters();
            if(lengthRestrictions != null && shipmentUnit.getLength() != null){
                if(lengthRestrictions.check(shipmentUnit.getLength()) != 0){
                    throw new ValidationException("order shipment unit should have a length in range {0}, {1}",
                            lengthRestrictions.getMinValue().toString(), lengthRestrictions.getMaxValue().toString());
                }
            }
            BigDecimalRange heightRestrictions = restrictions.get().getHeightRangeInCentimeters();
            if(heightRestrictions != null && shipmentUnit.getHeight() != null){
                if(heightRestrictions.check(shipmentUnit.getHeight()) != 0){
                    throw new ValidationException("order shipment unit should have a height in range {0}, {1}",
                            heightRestrictions.getMinValue().toString(), heightRestrictions.getMaxValue().toString());
                }
            }

        }

    }
}
