package ekol.orders.order.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderDocument;

import java.util.List;
import java.util.Optional;

public interface OrderDocumentRepository extends ApplicationRepository<OrderDocument> {

    Optional<OrderDocument> findById(Long id);

    List<OrderDocument> findByOrderId(Long id);

}
