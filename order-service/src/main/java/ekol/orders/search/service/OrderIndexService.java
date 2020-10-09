package ekol.orders.search.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.repository.OrderShipmentRepository;
import ekol.orders.search.domain.ShipmentSearchDocument;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class OrderIndexService {

	private OrderShipmentRepository shipmentRepository;
	private ElasticsearchTemplate elasticsearchTemplate;
	private ShipmentIndexDocumentConverter documentConverter;

	@PostConstruct
	private void init() {
		checkIndexAndCreate();
	}

	private void checkIndexAndCreate(){
		if (!elasticsearchTemplate.indexExists(ShipmentSearchDocument.class)) {
			elasticsearchTemplate.createIndex(ShipmentSearchDocument.class);
			elasticsearchTemplate.putMapping(ShipmentSearchDocument.class);
			elasticsearchTemplate.refresh(ShipmentSearchDocument.class);
		}
	}

	public void reindexShipments() {
		elasticsearchTemplate.deleteIndex(ShipmentSearchDocument.class);
		checkIndexAndCreate();
		shipmentRepository.findAll().forEach(this::indexShipment);
	}

	public void indexOrder(String code) {
		checkIndexAndCreate();
		shipmentRepository.findWithOrderByOrderCode(code).stream().map(documentConverter::convert).filter(Objects::nonNull).forEach(this::indexShipmentDocument);
	}

	public void indexOrder(Order order) {
		checkIndexAndCreate();
		order.getShipments().stream().map(documentConverter::convert).filter(Objects::nonNull).forEach(this::indexShipmentDocument);
	}
	
	public void indexOrder(Long orderId) {
		checkIndexAndCreate();
		shipmentRepository.findWithOrderByOrderId(orderId).stream().map(documentConverter::convert).filter(Objects::nonNull).forEach(this::indexShipmentDocument);
	}

	public void indexShipment(Long shipmentId) {
		checkIndexAndCreate();
		shipmentRepository.findWithOrderById(shipmentId).map(documentConverter::convert).ifPresent(this::indexShipmentDocument);
	}

	public void indexShipment(String code) {
		checkIndexAndCreate();
		shipmentRepository.findWithOrderByCode(code).map(documentConverter::convert).ifPresent(this::indexShipmentDocument);
	}

	public void indexShipment(OrderShipment shipment) {
		checkIndexAndCreate();
		Optional.of(shipment).map(documentConverter::convert).ifPresent(this::indexShipmentDocument);
	}

	private void indexShipmentDocument(ShipmentSearchDocument document){
		elasticsearchTemplate.index(new IndexQueryBuilder().withId(document.getId()).withObject(document).build());
	}
}
