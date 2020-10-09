package ekol.orders.order.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentEquipmentRequirement;
import ekol.orders.order.repository.OrderShipmentEquipmentRequirementRepository;
import ekol.orders.order.validator.OrderShipmentEquipmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderShipmentEquipmentService {

    private ListOrderShipmentService orderShipmentService;
    private OrderShipmentEquipmentValidator shipmentEquipmentValidator;
    private OrderShipmentEquipmentRequirementRepository equipmentRequirementRepository;

    @Autowired
    public OrderShipmentEquipmentService(ListOrderShipmentService orderShipmentService,
                                         OrderShipmentEquipmentRequirementRepository equipmentRequirementRepository,
                                         OrderShipmentEquipmentValidator shipmentEquipmentValidator){
        this.orderShipmentService = orderShipmentService;
        this.equipmentRequirementRepository = equipmentRequirementRepository;
        this.shipmentEquipmentValidator = shipmentEquipmentValidator;
    }

    private OrderShipmentEquipmentRequirement getOrThrowException(Long id){
        return equipmentRequirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment requirement with id {0} does not exist", String.valueOf(id)));
    }


    public OrderShipment saveEquipmentRequirement(Long shipmentId, OrderShipmentEquipmentRequirement requirement){
        shipmentEquipmentValidator.validate(requirement);
        OrderShipment shipment = orderShipmentService.getOrThrowException(shipmentId);
        if(requirement.getId() != null){
            getOrThrowException(requirement.getId());
        }
        requirement.setShipment(shipment);
        equipmentRequirementRepository.save(requirement);
        return shipment;
    }

    public OrderShipment deleteEquipmentRequirement(Long shipmentId, Long requirementId){
        OrderShipment shipment = orderShipmentService.getOrThrowException(shipmentId);
        OrderShipmentEquipmentRequirement requirement = getOrThrowException(requirementId);
        requirement.setDeleted(true);
        equipmentRequirementRepository.save(requirement);
        return shipment;
    }
}
