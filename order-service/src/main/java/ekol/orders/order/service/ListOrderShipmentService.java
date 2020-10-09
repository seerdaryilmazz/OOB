package ekol.orders.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.repository.OrderShipmentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ListOrderShipmentService {

    private OrderShipmentRepository shipmentRepository;

    public OrderShipment getByCodeOrThrowException(String shipmentCode){
    	return shipmentRepository.findByCode(shipmentCode)
    			.orElseThrow(() -> new ResourceNotFoundException("Shipment with code {0} not found", shipmentCode));
    }
    
    public OrderShipment getOrThrowException(Long id){
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with id {0} not found", String.valueOf(id)));
    }

    public OrderShipment getWithUnitsOrThrowException(Long id){
        return shipmentRepository.findWithUnitsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with id {0} not found", String.valueOf(id)));
    }

    public OrderShipment getWithOrderOrThrowException(Long id){
        return shipmentRepository.findWithOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with id {0} not found", String.valueOf(id)));
    }

}
