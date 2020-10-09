package ekol.orders.order.service;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentIdMapping;
import ekol.orders.order.domain.dto.json.updateOrder.ShipmentIdMappingJson;
import ekol.orders.order.repository.OrderShipmentIdMappingRepository;
import ekol.orders.search.service.OrderIndexService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class OrderShipmentIdMappingService {

    private ListOrderShipmentService listOrderShipmentService;
    private OrderShipmentIdMappingRepository orderShipmentIdMappingRepository;
    private OrderIndexService orderIndexService;
	
	public OrderShipment updateApplicationIdMapping(String shipmentCode, ShipmentIdMappingJson idMappingJson) {
		idMappingJson.setApplication(idMappingJson.getApplication().toUpperCase());
		
    	OrderShipment shipment = listOrderShipmentService.getByCodeOrThrowException(shipmentCode);
    	List<OrderShipmentIdMapping> mappings = orderShipmentIdMappingRepository.findByShipmentCodeAndApplication(shipmentCode, idMappingJson.getApplication());
    	
    	Supplier<Stream<OrderShipmentIdMapping>> mappinngSupplier = mappings::stream;
    	Predicate<OrderShipmentIdMapping> predicate = m->Objects.equals(m.getApplicationOrderShipmentId(), idMappingJson.getApplicationId());
    	
    	if(mappinngSupplier.get().noneMatch(predicate)) {
    		mappings.forEach(t->t.setDeleted(true));
    		orderShipmentIdMappingRepository.save(mappings);
    		
    		OrderShipmentIdMapping item = OrderShipmentIdMapping.withApplication(idMappingJson.getApplication(), idMappingJson.getApplicationId());
    		item.setShipment(shipment);
    		orderShipmentIdMappingRepository.save(item);
    		orderIndexService.indexShipment(shipment.getId());
    	}
    	return shipment;
    }
}
