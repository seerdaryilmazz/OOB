package ekol.orders.transportOrder.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.transportOrder.domain.Shipment;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface ShipmentRepository extends ApplicationRepository<Shipment> {

    @EntityGraph(value = "Shipment.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Shipment findWithDetailsById(Long id);

    @EntityGraph(value = "Shipment.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<Shipment> findWithDetailsByTransportOrderId(Long id);

    @EntityGraph(value = "Shipment.withTransportOrder", type = EntityGraph.EntityGraphType.LOAD)
    Shipment findWithTransportOrderById(Long id);

    Shipment findOneByIdAndTransportOrderSubsidiaryId(Long shipmentId, Long subsidiaryId);

    Shipment findByCode(String code);

}
