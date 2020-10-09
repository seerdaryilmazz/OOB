package ekol.orders.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.repository.OrderRepository;
import ekol.orders.order.repository.OrderShipmentRepository;

@Service
public class ListOrderService {
    private OrderRepository orderRepository;
    private OrderShipmentRepository orderShipmentRepository;

    @Autowired
    public ListOrderService(OrderRepository orderRepository, OrderShipmentRepository orderShipmentRepository){
        this.orderRepository = orderRepository;
        this.orderShipmentRepository = orderShipmentRepository;
    }


    public Order findByIdOrThrowException(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id {0} not found", id.toString()));
    }

    public OrderShipment findShipmentByIdOrThrowException(Long id){
        return orderShipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with id {0} not found", id.toString()));
    }

    public Order findByIdWithShipmentsOrThrowException(Long id){
        return orderRepository.findWithShipmentsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id {0} not found", id.toString()));
    }

    public Order findByCodeWithShipmentsOrThrowException(String code){
        return orderRepository.findWithShipmentsByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Order with code {0} not found", code));
    }


	public Order findByShipmentCodeWithShipmentOrThrowException(String shipmentCode) {
		OrderShipment orderShipment = orderShipmentRepository.findByCode(shipmentCode).orElseThrow(()->new ResourceNotFoundException("Shipment with code {0} not found", shipmentCode));
		return orderShipment.getOrder();
	}
}
