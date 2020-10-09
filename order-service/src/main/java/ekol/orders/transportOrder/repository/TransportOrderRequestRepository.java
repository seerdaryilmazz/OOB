package ekol.orders.transportOrder.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface TransportOrderRequestRepository extends ApplicationRepository<TransportOrderRequest> {

    @EntityGraph(value = "TransportOrderRequest.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    TransportOrderRequest findWithDetailsById(Long id);

    @EntityGraph(value = "TransportOrderRequest.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    TransportOrderRequest findWithDetailsByOrderId(Long orderId);

    List<TransportOrderRequest> findByOrderIsNull();

    @EntityGraph(value = "TransportOrderRequest.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<TransportOrderRequest> findTop10ByCreatedByIdOrderByLastUpdatedDesc(Long createdById);

    List<TransportOrderRequest> findAllByOfferNo(String offerNo);

}
