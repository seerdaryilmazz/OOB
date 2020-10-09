package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by burak on 24/11/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleAppropriateValidationRequest {

    private VehicleFeatureFilter vehicleFeatures;
    private List<VehicleFeatureFilter> requiredVehicleFeatures;
    private List<VehicleFeatureFilter> notAllowedVehicleFeatures;

    public VehicleFeatureFilter getVehicleFeatures() {
        return vehicleFeatures;
    }

    public void setVehicleFeatures(VehicleFeatureFilter vehicleFeatures) {
        this.vehicleFeatures = vehicleFeatures;
    }

    public List<VehicleFeatureFilter> getRequiredVehicleFeatures() {
        return requiredVehicleFeatures;
    }

    public void setRequiredVehicleFeatures(List<VehicleFeatureFilter> requiredVehicleFeatures) {
        this.requiredVehicleFeatures = requiredVehicleFeatures;
    }

    public List<VehicleFeatureFilter> getNotAllowedVehicleFeatures() {
        return notAllowedVehicleFeatures;
    }

    public void setNotAllowedVehicleFeatures(List<VehicleFeatureFilter> notAllowedVehicleFeatures) {
        this.notAllowedVehicleFeatures = notAllowedVehicleFeatures;
    }
}
