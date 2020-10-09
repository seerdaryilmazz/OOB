package ekol.orders.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.dto.json.updateOrder.ShipmentUnloadingAppointment;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateOrderStatusRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateServiceTypeRequestJson;
import ekol.orders.order.domain.dto.json.updateOrder.UpdateTruckLoadTypeRequestJson;
import ekol.orders.order.domain.dto.response.kartoteks.LocationResponse;
import ekol.orders.order.repository.OrderRepository;
import ekol.orders.order.repository.OrderShipmentRepository;
import ekol.orders.order.validator.OrderShipmentDateValidator;
import ekol.orders.search.service.OrderIndexService;

@Service
public class UpdateOrderService {

    private OrderRepository orderRepository;
    private OrderShipmentRepository shipmentRepository;
    private ListOrderService listOrderService;
    private ProjectServiceClient projectServiceClient;
    private KartoteksServiceClient kartoteksServiceClient;
    private OrderShipmentDateValidator orderShipmentDateValidator;
    private OrderIndexService orderIndexService;

    @Autowired
    public UpdateOrderService(OrderRepository orderRepository,
                              OrderShipmentRepository shipmentRepository,
                              ListOrderService listOrderService,
                              ProjectServiceClient projectServiceClient,
                              KartoteksServiceClient kartoteksServiceClient,
                              OrderShipmentDateValidator orderShipmentDateValidator,
                              OrderIndexService orderIndexService){
        this.orderRepository = orderRepository;
        this.shipmentRepository = shipmentRepository;
        this.listOrderService = listOrderService;
        this.projectServiceClient = projectServiceClient;
        this.kartoteksServiceClient = kartoteksServiceClient;
        this.orderShipmentDateValidator = orderShipmentDateValidator;
        this.orderIndexService = orderIndexService;
    }

    @Transactional
    public Order updateServiceTypeAndUnloadingAppointments(Long orderId, UpdateServiceTypeRequestJson updateServiceTypeRequest){
        Order order = listOrderService.findByIdOrThrowException(orderId);
        order.setServiceType(updateServiceTypeRequest.getServiceType());
        calculateDeliveryDateForAllShipments(order);
        updateUnloadingAppointments(updateServiceTypeRequest.getUnloadingAppointments());
        Order savedOrder = orderRepository.save(order);
        orderIndexService.indexOrder(orderId);
        return savedOrder;
    }

    @Transactional
    public Order updateTruckLoadTypeAndUnloadingAppointments(Long orderId, UpdateTruckLoadTypeRequestJson updateTruckLoadTypeRequest){
        Order order = listOrderService.findByIdOrThrowException(orderId);
        order.setTruckLoadType(updateTruckLoadTypeRequest.getTruckLoadType());
        calculateDeliveryDateForAllShipments(order);
        updateUnloadingAppointments(updateTruckLoadTypeRequest.getUnloadingAppointments());
        Order savedOrder = orderRepository.save(order);
        orderIndexService.indexOrder(orderId);
        return savedOrder;
    }
    
    @Transactional
    public Order updateStatus(Long orderId, UpdateOrderStatusRequestJson updateOrderStatusJson ) {
    	Order order = listOrderService.findByIdOrThrowException(orderId);
    	order.setStatus(updateOrderStatusJson.getStatus());
    	Order savedOrder = orderRepository.save(order);
        orderIndexService.indexOrder(orderId);
        return savedOrder;
    }

    private void calculateDeliveryDateForAllShipments(Order order){
        order.getShipments().forEach(shipment -> {
            LocalDateTime deliveryDateLocal = projectServiceClient.calculateDeliveryDate(order, shipment);
            FixedZoneDateTime deliveryDate = null;
            if(deliveryDateLocal != null){
                LocationResponse unloadingLocation =
                        kartoteksServiceClient.getLocation(shipment.getConsignee().getHandlingLocation().getId());
                deliveryDate = new FixedZoneDateTime(deliveryDateLocal, unloadingLocation.getTimezone());
            }
            shipment.setDeliveryDate(deliveryDate);
        });
    }

    private void updateUnloadingAppointments(List<ShipmentUnloadingAppointment> appointments){
        appointments.forEach(shipmentAndAppointment -> {
            OrderShipment shipment = listOrderService.findShipmentByIdOrThrowException(shipmentAndAppointment.getShipmentId());
            shipment.setUnloadingAppointment(
                    shipmentAndAppointment.isDeleted() ? null : shipmentAndAppointment.getAppointment().toEntity()
            );

            orderShipmentDateValidator.validate(shipment);
            shipmentRepository.save(shipment);
        });
    }
}
