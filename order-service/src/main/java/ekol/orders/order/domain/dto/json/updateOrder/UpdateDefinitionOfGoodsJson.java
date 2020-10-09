package ekol.orders.order.domain.dto.json.updateOrder;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import ekol.orders.order.domain.dto.json.OrderShipmentDefinitionOfGoodsJson;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateDefinitionOfGoodsJson {
	private List<OrderShipmentDefinitionOfGoodsJson> definitionOfGoods = new ArrayList<>();
}
