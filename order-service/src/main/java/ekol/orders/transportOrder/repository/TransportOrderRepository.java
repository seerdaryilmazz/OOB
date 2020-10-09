package ekol.orders.transportOrder.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.transportOrder.domain.TransportOrder;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.stream.Stream;

public interface TransportOrderRepository extends ApplicationRepository<TransportOrder> {

    /**
     * Bu metodun ismini değiştirirken dikkat etmek gerekli. Daha detaylı bilgi için
     * aşağıdaki sayfada "findAllDistinctBy" geçen bölüme bakınız.
     *
     * https://jira.spring.io/browse/DATAJPA-680
     */

/*    @EntityGraph(value = "TransportOrder.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<TransportOrder> findWithDetailsDistinctByRequestSubsidiariesNameIn(Set<String> subsidiariesId);


    @EntityGraph(value = "TransportOrder.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<TransportOrder> findWithDetailsDistinctByRequestSubsidiariesIdIn(Set<Long> subsidiariesId);*/


    @EntityGraph(value = "TransportOrder.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<TransportOrder> findAllWithDetailsDistinctBy();

    @EntityGraph(value = "TransportOrder.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    TransportOrder findWithDetailsById(Long id);

    @EntityGraph(value = "TransportOrder.ruleSet", type = EntityGraph.EntityGraphType.LOAD)
    TransportOrder findWithRuleSetDetailsById(Long id);


    @EntityGraph(value = "TransportOrder.forShipmentAssignments", type = EntityGraph.EntityGraphType.LOAD)
    List<TransportOrder> findWithAssignmentDetailsDistinctBySubsidiaryId(Long subsidiaryId);

    @EntityGraph(value = "TransportOrder.forShipmentAssignments", type = EntityGraph.EntityGraphType.LOAD)
    TransportOrder findTransportOrderByIdAndSubsidiaryId(Long transportOrderId, Long subsidiaryId);

    Stream<TransportOrder> findStreamingByIdIsNotNull();


}
