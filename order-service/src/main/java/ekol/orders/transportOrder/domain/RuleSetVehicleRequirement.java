package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleSetVehicleRequirement {

    private Long shipmentUnitId;
    private Set<String> requiredVehicles = new HashSet<>();
    private Set<String> notAllowedVehicles = new HashSet<>();

    public Long getShipmentUnitId() {
        return shipmentUnitId;
    }

    public void setShipmentUnitId(Long shipmentUnitId) {
        this.shipmentUnitId = shipmentUnitId;
    }

    public Set<String> getRequiredVehicles() {
        return requiredVehicles;
    }

    public void setRequiredVehicles(Set<String> requiredVehicles) {
        this.requiredVehicles = requiredVehicles;
    }

    public Set<String> getNotAllowedVehicles() {
        return notAllowedVehicles;
    }

    public void setNotAllowedVehicles(Set<String> notAllowedVehicles) {
        this.notAllowedVehicles = notAllowedVehicles;
    }
}
