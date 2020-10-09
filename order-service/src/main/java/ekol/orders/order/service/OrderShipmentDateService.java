package ekol.orders.order.service;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.order.domain.Appointment;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.dto.response.kartoteks.LocationResponse;
import ekol.orders.order.repository.OrderShipmentRepository;
import ekol.orders.order.validator.OrderShipmentDateValidator;
import ekol.orders.search.service.OrderIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderShipmentDateService {

    private ListOrderShipmentService listOrderShipmentService;
    private OrderShipmentRepository shipmentRepository;
    private OrderShipmentDateValidator orderShipmentDateValidator;
    private ProjectServiceClient projectServiceClient;
    private KartoteksServiceClient kartoteksServiceClient;
    private OrderIndexService orderIndexService;

    @Autowired
    public OrderShipmentDateService(ListOrderShipmentService listOrderShipmentService,
                                    OrderShipmentRepository shipmentRepository,
                                    OrderShipmentDateValidator orderShipmentDateValidator,
                                    ProjectServiceClient projectServiceClient,
                                    KartoteksServiceClient kartoteksServiceClient,
                                    OrderIndexService orderIndexService){

        this.listOrderShipmentService = listOrderShipmentService;
        this.shipmentRepository = shipmentRepository;
        this.orderShipmentDateValidator = orderShipmentDateValidator;
        this.projectServiceClient = projectServiceClient;
        this.kartoteksServiceClient = kartoteksServiceClient;
        this.orderIndexService = orderIndexService;
    }

    public OrderShipment updateReadyDate(Long id, FixedZoneDateTime readyDate, Appointment unloadingAppointment){
        OrderShipment shipment = listOrderShipmentService.getWithOrderOrThrowException(id);
        shipment.setReadyAtDate(readyDate);
        shipment.setLoadingAppointment(null);
        shipment.setUnloadingAppointment(unloadingAppointment);
        updateDeliveryDate(shipment);
        orderShipmentDateValidator.validate(shipment);
        OrderShipment savedShipment = shipmentRepository.save(shipment);
        orderIndexService.indexShipment(id);
        return savedShipment;
    }

    public OrderShipment updateLoadingAppointment(Long id, Appointment loadingAppointment, Appointment unloadingAppointment){
        OrderShipment shipment = listOrderShipmentService.getWithOrderOrThrowException(id);
        shipment.setReadyAtDate(null);
        shipment.setLoadingAppointment(loadingAppointment);
        shipment.setUnloadingAppointment(unloadingAppointment);
        updateDeliveryDate(shipment);
        orderShipmentDateValidator.validate(shipment);
        OrderShipment savedShipment = shipmentRepository.save(shipment);
        orderIndexService.indexShipment(id);
        return savedShipment;
    }

    public OrderShipment updateUnloadingAppointment(Long id, Appointment appointment){
        OrderShipment shipment = listOrderShipmentService.getWithOrderOrThrowException(id);
        shipment.setUnloadingAppointment(appointment);
        orderShipmentDateValidator.validate(shipment);
        OrderShipment savedShipment = shipmentRepository.save(shipment);
        orderIndexService.indexShipment(id);
        return savedShipment;
    }

    public OrderShipment deleteUnloadingAppointment(Long id){
        OrderShipment shipment = listOrderShipmentService.getWithOrderOrThrowException(id);
        shipment.setUnloadingAppointment(null);
        orderShipmentDateValidator.validate(shipment);
        OrderShipment savedShipment = shipmentRepository.save(shipment);
        orderIndexService.indexShipment(id);
        return savedShipment;
    }

    private void updateDeliveryDate(OrderShipment shipment){
        LocalDateTime deliveryDateLocal = projectServiceClient.calculateDeliveryDate(shipment.getOrder(), shipment);
        FixedZoneDateTime deliveryDate = null;
        if(deliveryDateLocal != null){
            LocationResponse unloadingLocation =
                    kartoteksServiceClient.getLocation(shipment.getConsignee().getHandlingLocation().getId());
            deliveryDate = new FixedZoneDateTime(deliveryDateLocal, unloadingLocation.getTimezone());
        }
        shipment.setDeliveryDate(deliveryDate);
    }
}
