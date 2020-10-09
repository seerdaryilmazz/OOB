package ekol.orders.order.service;

import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentUnit;
import ekol.orders.order.domain.dto.response.ShipmentLoadSpecRuleResponse;
import ekol.orders.order.domain.dto.response.UnitLoadSpecRuleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderShipmentLoadSpecsService {

    private OrderTemplateServiceClient orderTemplateServiceClient;

    @Autowired
    public OrderShipmentLoadSpecsService(OrderTemplateServiceClient orderTemplateServiceClient){
        this.orderTemplateServiceClient = orderTemplateServiceClient;
    }

    public void setLoadSpecsForOrder(Order order) {
        order.getShipments().forEach(this::setLoadSpecsForShipment);
    }
    public void setLoadSpecsForShipment(OrderShipment shipment) {
        if(shipment.getGrossWeight() != null && shipment.getTotalLdm() != null &&
                shipment.getValueOfGoods() != null && shipment.getValueOfGoods().getAmount() != null){
            ShipmentLoadSpecRuleResponse specRuleResponse =
                    orderTemplateServiceClient.runLoadSpecRulesForShipment(
                            shipment.getGrossWeight(), shipment.getTotalLdm(), shipment.getValueOfGoods().getAmount());
            shipment.setHeavyLoad(specRuleResponse.isHeavyLoad());
            shipment.setValuableLoad(specRuleResponse.isValuableLoad());
        }
        shipment.getUnits().stream()
                .filter(shipmentUnit -> shipmentUnit.getWidth() != null && shipmentUnit.getLength() != null &&
                        shipmentUnit.getHeight() != null)
                .forEach(shipmentUnit -> {
                    UnitLoadSpecRuleResponse response = orderTemplateServiceClient.runLoadSpecRulesForUnit(
                            shipmentUnit.getWidth(), shipmentUnit.getLength(), shipmentUnit.getHeight());
                    shipmentUnit.setLongLoad(response.isLongLoad());
                    shipmentUnit.setOversizeLoad(response.isOversizeLoad());
                });
        shipment.setLongLoad(
                shipment.getUnits().stream().anyMatch(OrderShipmentUnit::isLongLoad));
        shipment.setOversizeLoad(
                shipment.getUnits().stream().anyMatch(OrderShipmentUnit::isOversizeLoad));
    }
}
