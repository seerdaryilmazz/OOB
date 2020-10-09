package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 24/11/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleFeaturesValidationRequest {

    private List<VehicleFeatureFilter> requiredVehicleFeatures = new ArrayList<>();
    private List<VehicleFeatureFilter> notAllowedVehicleFeatures = new ArrayList<>();

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
