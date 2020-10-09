package ekol.orders.order.repository;

import java.util.List;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.order.domain.OrderShipmentIdMapping;

public interface OrderShipmentIdMappingRepository extends ApplicationRepository<OrderShipmentIdMapping> {
	public List<OrderShipmentIdMapping> findByShipmentCodeAndApplication(String code, String application);
}
