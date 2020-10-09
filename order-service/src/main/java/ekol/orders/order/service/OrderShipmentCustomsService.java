package ekol.orders.order.service;

import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;
import ekol.orders.order.repository.OrderShipmentArrivalCustomsRepository;
import ekol.orders.order.repository.OrderShipmentDepartureCustomsRepository;
import ekol.orders.order.validator.OrderShipmentCustomsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderShipmentCustomsService {

    private OrderShipmentDepartureCustomsRepository departureCustomsRepository;
    private OrderShipmentArrivalCustomsRepository arrivalCustomsRepository;
    private ListOrderShipmentService orderShipmentService;
    private OrderShipmentCustomsValidator customsValidator;

    @Autowired
    public OrderShipmentCustomsService(OrderShipmentDepartureCustomsRepository departureCustomsRepository,
                                       OrderShipmentArrivalCustomsRepository arrivalCustomsRepository,
                                       OrderShipmentCustomsValidator customsValidator,
                                       ListOrderShipmentService orderShipmentService){
        this.departureCustomsRepository = departureCustomsRepository;
        this.arrivalCustomsRepository = arrivalCustomsRepository;
        this.customsValidator = customsValidator;
        this.orderShipmentService = orderShipmentService;
    }

    public OrderShipment saveArrivalCustoms(Long shipmentId, OrderShipmentArrivalCustoms customsDetails){
        OrderShipment shipment = orderShipmentService.getOrThrowException(shipmentId);
        customsDetails.setShipment(shipment);
        arrivalCustomsRepository.findByShipment(shipment)
                .ifPresent(arrivalCustoms -> customsDetails.setId(arrivalCustoms.getId()));

        if(shipment.getConsignee().isHandlingLocationTR()){
            customsValidator.validateArrivalToTR(customsDetails);
        }else{
            customsValidator.validateArrival(customsDetails);
        }

        OrderShipmentArrivalCustoms savedCustoms = arrivalCustomsRepository.save(customsDetails);
        shipment.setArrivalCustoms(savedCustoms);
        return shipment;

    }
    public OrderShipment saveDepartureCustoms(Long shipmentId, OrderShipmentDepartureCustoms customsDetails){
        OrderShipment shipment = orderShipmentService.getOrThrowException(shipmentId);
        customsDetails.setShipment(shipment);
        departureCustomsRepository.findByShipment(shipment)
                .ifPresent(departureCustoms -> customsDetails.setId(departureCustoms.getId()));
        if(shipment.getSender().isHandlingLocationTR()){
            customsValidator.validateDepartureFromTR(customsDetails);
        }else{
            customsValidator.validateDeparture(customsDetails);
        }
        OrderShipmentDepartureCustoms savedCustoms = departureCustomsRepository.save(customsDetails);
        shipment.setDepartureCustoms(savedCustoms);
        return shipment;
    }
}
