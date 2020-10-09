package ekol.orders.order.domain.dto.json;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.orders.order.domain.OrderShipmentDefinitionOfGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor(staticName="with")
@NoArgsConstructor
@EqualsAndHashCode
public class OrderShipmentDefinitionOfGoodsJson {
	
	private Long id;
    private String code;
    private String name;
    private Long hscodeId;
    
    @JsonIgnoreProperties
    public boolean isValid() {
        if(getId() == null || StringUtils.isEmpty(getName())) {
            return false;
        }
        return true;
    }
    
    public static OrderShipmentDefinitionOfGoodsJson with(OrderShipmentDefinitionOfGoods entity) {
    	return OrderShipmentDefinitionOfGoodsJson.with(entity.getId(), entity.getCode(), entity.getName(), entity.getHscodeId());
    }
}
