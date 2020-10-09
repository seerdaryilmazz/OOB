package ekol.orders.order.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.repository.OrderShipmentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class OrderShipmentOrderNumbersService {

    private OrderShipmentRepository shipmentRepository;

    public OrderShipment getWithOrderNumbersOrThrowException(Long id){
        return shipmentRepository.findWithOrderNumbersById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with id {0} not found", String.valueOf(id)));
    }

    public Set<String> findSenderOrderNumbers(Long shipmentId){
        return getWithOrderNumbersOrThrowException(shipmentId).getSenderOrderNumbers();
    }
    
    public Set<String> findConsigneeOrderNumbers(Long shipmentId){
        return getWithOrderNumbersOrThrowException(shipmentId).getConsigneeOrderNumbers();
    }
    
    public Set<String> findCustomerOrderNumbers(Long shipmentId){
        return getWithOrderNumbersOrThrowException(shipmentId).getCustomerOrderNumbers();
    }
    
    public Set<String> findLoadingOrderNumbers(Long shipmentId){
    	return getWithOrderNumbersOrThrowException(shipmentId).getLoadingOrderNumbers();
    }
    
    public Set<String> findUnloadingOrderNumbers(Long shipmentId){
    	return getWithOrderNumbersOrThrowException(shipmentId).getUnloadingOrderNumbers();
    }

    public OrderShipment updateCustomerOrderNumbers(Long id, Set<String> orderNumbers){
        OrderShipment shipment = getWithOrderNumbersOrThrowException(id);
        shipment.setCustomerOrderNumbers(orderNumbers);
        return shipmentRepository.save(shipment);
    }

    public OrderShipment updateSenderOrderNumbers(Long id, Set<String> orderNumbers){
        OrderShipment shipment = getWithOrderNumbersOrThrowException(id);
        shipment.setSenderOrderNumbers(orderNumbers);
        return shipmentRepository.save(shipment);
    }

    public OrderShipment updateConsigneeOrderNumbers(Long id, Set<String> orderNumbers){
        OrderShipment shipment = getWithOrderNumbersOrThrowException(id);
        shipment.setConsigneeOrderNumbers(orderNumbers);
        return shipmentRepository.save(shipment);
    }
    
    public OrderShipment updateLoadingOrderNumbers(Long id, Set<String> orderNumbers){
    	OrderShipment shipment = getWithOrderNumbersOrThrowException(id);
    	shipment.setLoadingOrderNumbers(orderNumbers);
    	return shipmentRepository.save(shipment);
    }
    
    public OrderShipment updateUnloadingOrderNumbers(Long id, Set<String> orderNumbers){
    	OrderShipment shipment = getWithOrderNumbersOrThrowException(id);
    	shipment.setUnloadingOrderNumbers(orderNumbers);
    	return shipmentRepository.save(shipment);
    }
}
