package ekol.orders.order.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentDefinitionOfGoods;
import ekol.orders.order.repository.OrderShipmentDefinitionOfGoodsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@Data
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class UpdateDefinitionOfGoodsService {


	private OrderShipmentDefinitionOfGoodsRepository orderShipmentDefinitionOfGoodsRepository;

	public void updateDefinitionOfGoods(OrderShipment shipment, List<OrderShipmentDefinitionOfGoods> newDefinitionOfGoods) {
		delete(shipment, newDefinitionOfGoods);
		update(shipment, newDefinitionOfGoods);
		add(shipment, newDefinitionOfGoods);
		if(!CollectionUtils.isEmpty(shipment.getDefinitionOfGoods())) {
			orderShipmentDefinitionOfGoodsRepository.save(shipment.getDefinitionOfGoods());
		}
	}

	private boolean isExistsInList(OrderShipmentDefinitionOfGoods definitionOfGoods, List<OrderShipmentDefinitionOfGoods> newDefinitionOfGoods){
		return newDefinitionOfGoods.parallelStream()
				.anyMatch(updatedOrderShipmentUnit -> definitionOfGoods.getId().equals(updatedOrderShipmentUnit.getId()));
	}

	private void delete(OrderShipment orderShipment, List<OrderShipmentDefinitionOfGoods> isExistsInList){
		orderShipment.getDefinitionOfGoods().parallelStream()
											.filter(definitionOfGoods -> !isExistsInList(definitionOfGoods, isExistsInList))
											.forEach(definitionOfGoods -> {
												definitionOfGoods.setDeleted(true);
												orderShipmentDefinitionOfGoodsRepository.save(definitionOfGoods);
											});
		orderShipment.setDefinitionOfGoods(orderShipment.getDefinitionOfGoods().parallelStream().filter(definitionOfGoods -> !definitionOfGoods.isDeleted()).collect(toList()));
	}
	
	private void update(OrderShipment orderShipment, List<OrderShipmentDefinitionOfGoods> newDefinitionOfGoods){
        orderShipment.getDefinitionOfGoods()
                .forEach(definitionOfGoods -> {
                	newDefinitionOfGoods.parallelStream()
                            .filter(updatedDefinitionOfGoods -> Objects.equals(definitionOfGoods.getId(), updatedDefinitionOfGoods.getId()))
                            .findFirst()
                            .ifPresent(updatedDefinitionOfGoods -> update(definitionOfGoods, updatedDefinitionOfGoods));
                });
    }
	
	private void update(OrderShipmentDefinitionOfGoods definitionOfGoods, OrderShipmentDefinitionOfGoods updatedDefinitionOfGoods){
		definitionOfGoods.setCode(updatedDefinitionOfGoods.getCode());
		definitionOfGoods.setName(updatedDefinitionOfGoods.getName());
    }
	
	private void add(OrderShipment orderShipment, List<OrderShipmentDefinitionOfGoods> newShipmentUnits){
        newShipmentUnits.parallelStream()
                .filter(definitionOfGoods -> Objects.isNull(definitionOfGoods.getId()))
                .forEach(definitionOfGoods -> {
                	definitionOfGoods.setShipment(orderShipment);
                	orderShipment.getDefinitionOfGoods().add(definitionOfGoods);
                });
    }
}
