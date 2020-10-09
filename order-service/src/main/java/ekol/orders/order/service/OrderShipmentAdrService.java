package ekol.orders.order.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.order.domain.OrderShipmentAdr;
import ekol.orders.order.repository.OrderShipmentAdrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderShipmentAdrService {

    private OrderShipmentAdrRepository shipmentAdrRepository;

    @Autowired
    public OrderShipmentAdrService(OrderShipmentAdrRepository shipmentAdrRepository){
        this.shipmentAdrRepository = shipmentAdrRepository;
    }

    public OrderShipmentAdr findByIdOrThrowException(Long id){
        return shipmentAdrRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment ADR with id {0} does not exist", String.valueOf(id)));
    }

    public OrderShipmentAdr save(OrderShipmentAdr shipmentAdr){
        if(shipmentAdr.getId() == null){
            return insert(shipmentAdr);
        }else{
            return update(shipmentAdr);
        }
    }

    private OrderShipmentAdr insert(OrderShipmentAdr shipmentAdr) {
        return shipmentAdrRepository.save(shipmentAdr);
    }
    private OrderShipmentAdr update(OrderShipmentAdr shipmentAdr) {
        return shipmentAdrRepository.save(shipmentAdr);
    }

}
