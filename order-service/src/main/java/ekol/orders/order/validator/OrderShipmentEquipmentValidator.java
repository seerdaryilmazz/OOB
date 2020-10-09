package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentEquipmentRequirement;
import ekol.orders.transportOrder.domain.EquipmentType;
import ekol.orders.transportOrder.repository.EquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderShipmentEquipmentValidator {

    private EquipmentTypeRepository equipmentTypeRepository;

    @Autowired
    public OrderShipmentEquipmentValidator(EquipmentTypeRepository equipmentTypeRepository){
        this.equipmentTypeRepository = equipmentTypeRepository;
    }

    public void validate(OrderShipment shipment){
        shipment.getEquipmentRequirements().forEach(this::validate);
    }

    public void validate(OrderShipmentEquipmentRequirement requirement){
        if(requirement.getEquipment() == null || requirement.getEquipment().getId() == null){
            throw new ValidationException("order shipment equipment requirement should have equipment type");
        }
        Optional<EquipmentType> equipmentType = equipmentTypeRepository.findById(requirement.getEquipment().getId());
        if(!equipmentType.isPresent()){
            throw new ValidationException("Equipment type with id {0} does not exist", requirement.getEquipment().getId());
        }
        if(requirement.getCount() == null || requirement.getCount() <= 0){
            throw new ValidationException("order shipment equipment requirement should have count");
        }
    }
}
