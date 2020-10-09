package ekol.orders.order.domain.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.order.domain.OrderShipmentUnit;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShipmentUnitJson {

    private Long id;
    private Integer quantity;
    private IdCodeNameTrio packageType;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal height;
    private Integer stackSize;
    private String templateId;

    public OrderShipmentUnit toEntity(){
        OrderShipmentUnit shipmentUnit = new OrderShipmentUnit();
        shipmentUnit.setId(getId());
        shipmentUnit.setQuantity(getQuantity());
        if(getPackageType() != null){
            shipmentUnit.setPackageType(PackageType.with(getPackageType()));
        }
        shipmentUnit.setWidth(getWidth());
        shipmentUnit.setLength(getLength());
        shipmentUnit.setHeight(getHeight());
        shipmentUnit.setStackSize(getStackSize());
        shipmentUnit.setTemplateId(getTemplateId());
        return shipmentUnit;
    }

    public static OrderShipmentUnitJson fromEntity(OrderShipmentUnit shipmentUnit){
        OrderShipmentUnitJson json = new OrderShipmentUnitJson();
        json.setId(shipmentUnit.getId());
        json.setQuantity(shipmentUnit.getQuantity());
        if(shipmentUnit.getPackageType() != null){
            json.setPackageType(IdCodeNameTrio.with(shipmentUnit.getPackageType().getId(), shipmentUnit.getPackageType().getCode(), shipmentUnit.getPackageType().getName()));
        }
        json.setWidth(shipmentUnit.getWidth());
        json.setLength(shipmentUnit.getLength());
        json.setHeight(shipmentUnit.getHeight());
        json.setStackSize(shipmentUnit.getStackSize());
        json.setTemplateId(shipmentUnit.getTemplateId());
        return json;
    }
}
