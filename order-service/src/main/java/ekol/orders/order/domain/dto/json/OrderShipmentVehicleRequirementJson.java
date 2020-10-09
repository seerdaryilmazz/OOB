package ekol.orders.order.domain.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.order.domain.OrderShipmentVehicleRequirement;
import ekol.orders.order.domain.VehicleRequirementReason;
import ekol.orders.transportOrder.domain.OrderPlanningOperationType;
import ekol.orders.transportOrder.domain.VehicleFeature;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShipmentVehicleRequirementJson {

    private Long id;
    private VehicleFeature requirement;
    private OrderPlanningOperationType operationType;
    private VehicleRequirementReason requirementReason;

    public OrderShipmentVehicleRequirement toEntity(){
        OrderShipmentVehicleRequirement requirement = new OrderShipmentVehicleRequirement();
        requirement.setId(getId());
        requirement.setOperationType(getOperationType());
        requirement.setRequirement(getRequirement());
        requirement.setRequirementReason(getRequirementReason());
        return requirement;
    }

    public static OrderShipmentVehicleRequirementJson fromEntity(OrderShipmentVehicleRequirement requirement){
        OrderShipmentVehicleRequirementJson json = new OrderShipmentVehicleRequirementJson();
        json.setId(requirement.getId());
        json.setOperationType(requirement.getOperationType());
        json.setRequirement(requirement.getRequirement());
        json.setRequirementReason(requirement.getRequirementReason());
        return json;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleFeature getRequirement() {
        return requirement;
    }

    public void setRequirement(VehicleFeature requirement) {
        this.requirement = requirement;
    }

    public OrderPlanningOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OrderPlanningOperationType operationType) {
        this.operationType = operationType;
    }

    public VehicleRequirementReason getRequirementReason() {
        return requirementReason;
    }

    public void setRequirementReason(VehicleRequirementReason requirementReason) {
        this.requirementReason = requirementReason;
    }
}
