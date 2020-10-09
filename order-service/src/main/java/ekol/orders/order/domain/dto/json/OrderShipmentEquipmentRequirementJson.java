package ekol.orders.order.domain.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import ekol.orders.order.domain.OrderShipmentEquipmentRequirement;
import ekol.orders.transportOrder.domain.EquipmentType;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShipmentEquipmentRequirementJson {

    private Long id;
    private IdCodeNameTrio equipment;
    private Integer count;

    public OrderShipmentEquipmentRequirement toEntity(){
        OrderShipmentEquipmentRequirement requirement = new OrderShipmentEquipmentRequirement();
        requirement.setId(getId());
        requirement.setEquipment(EquipmentType.with(getEquipment()));
        requirement.setCount(getCount());
        return requirement;
    }

    public static OrderShipmentEquipmentRequirementJson fromEntity(OrderShipmentEquipmentRequirement requirement){
        OrderShipmentEquipmentRequirementJson json = new OrderShipmentEquipmentRequirementJson();
        json.setId(requirement.getId());
        json.setEquipment(requirement.getEquipment().toIdCodeNameTrio());
        json.setCount(requirement.getCount());
        return json;
    }
}
