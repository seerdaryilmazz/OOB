package ekol.orders.transportOrder.controller;

import ekol.exceptions.ValidationException;
import ekol.orders.transportOrder.domain.validator.VehicleRequirementValidator;
import ekol.orders.transportOrder.domain.VehicleFeature;
import ekol.orders.transportOrder.dto.VehicleAppropriateValidationRequest;
import ekol.orders.transportOrder.dto.VehicleFeatureFilter;
import ekol.orders.transportOrder.dto.VehicleFeaturesValidationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vehicle-feature")
public class VehicleFeatureConflictionController {

    @Autowired
    private VehicleRequirementValidator vehicleRequirementValidator;


    /**
     * @description validates if required and notAllowed features are valid to coocur together
     * @param vehicleFeaturesValidationRequest
     * @return
     */
    @RequestMapping(value = "/validate/feature-confliction", method = RequestMethod.POST)
    public String isFeaturesConflict(@RequestBody VehicleFeaturesValidationRequest vehicleFeaturesValidationRequest) {

        VehicleFeatureFilter requiredFilter = new VehicleFeatureFilter();
        vehicleFeaturesValidationRequest.getRequiredVehicleFeatures().stream().forEach(feature -> requiredFilter.merge(feature));

        VehicleFeatureFilter notAllowedFilter = new VehicleFeatureFilter();
        vehicleFeaturesValidationRequest.getNotAllowedVehicleFeatures().stream().forEach(feature -> notAllowedFilter.merge(feature));

        String message = "";
        try {
            VehicleFeature.validateCooccurence(VehicleFeature.fromVehicleFeatureFilter(requiredFilter), VehicleFeature.fromVehicleFeatureFilter(notAllowedFilter));

        } catch(ValidationException e) {
            message = e.getMessage();
        }

        return message;
    }

    @RequestMapping(value = "/validate/feature-set", method = RequestMethod.GET)
    public void isFeaturesConflict(@RequestParam(name = "requiredFeatures", required = false) List<VehicleFeature> requiredFeatures,
                                     @RequestParam(name = "notAllowedFeatures", required = false) List<VehicleFeature> notAllowedFeatures) {

        VehicleFeature.validateCooccurence(requiredFeatures, notAllowedFeatures);
    }

    @RequestMapping(value = "/validate/features", method = RequestMethod.GET)
    public void isFeaturesConflict(@RequestParam(name = "features", required = false) List<VehicleFeature> features) {

        VehicleFeature.validateCooccurence(features);
    }

    /**
     * @description validates if give vehicle features are appropriate for given required and notAllowed features
     * @param vehicleAppropriateValidationRequest
     * @return
     */
    @RequestMapping(value = "/validate/vehicle-appropriate", method = RequestMethod.POST)
    public String isVehicleAppropriate(@RequestBody VehicleAppropriateValidationRequest vehicleAppropriateValidationRequest) {


        VehicleFeatureFilter vehicleFeatureFilter = vehicleAppropriateValidationRequest.getVehicleFeatures();
        List<VehicleFeature> vehicleFeatures = vehicleFeatureFilter != null ? VehicleFeature.fromVehicleFeatureFilter(vehicleFeatureFilter) : new ArrayList<>();

        VehicleFeatureFilter requiredFilter = new VehicleFeatureFilter();
        if(vehicleAppropriateValidationRequest.getRequiredVehicleFeatures() != null) {
            vehicleAppropriateValidationRequest.getRequiredVehicleFeatures().stream().forEach(feature -> requiredFilter.merge(feature));
        }
        List<VehicleFeature> requiredFeatures = VehicleFeature.fromVehicleFeatureFilter(requiredFilter);

        VehicleFeatureFilter notAllowedFilter = new VehicleFeatureFilter();
        if(vehicleAppropriateValidationRequest.getNotAllowedVehicleFeatures() != null) {
            vehicleAppropriateValidationRequest.getNotAllowedVehicleFeatures().stream().forEach(feature -> notAllowedFilter.merge(feature));
        }
        List<VehicleFeature> notAllowedFeatures = VehicleFeature.fromVehicleFeatureFilter(notAllowedFilter);

        String errorMessage = "";
        try {
            if(requiredFeatures != null) {
                //error: when vehicle does not have any of the required feature
                requiredFeatures.stream().filter(requiredFeature -> !vehicleFeatures.contains(requiredFeature)).findFirst().ifPresent(
                        requiredFeature -> {
                            throw new ValidationException("Following feature does not exist in the selected vehicle: " + requiredFeature);
                        }
                );
            }

            if(notAllowedFeatures != null) {
                //error: when vehicle have any of the not allowed feature
                notAllowedFeatures.stream().filter(notAllowedFeature -> vehicleFeatures.contains(notAllowedFeature)).findFirst().ifPresent(
                        notAllowedFeature -> {
                            throw new ValidationException("Following feature shouldn't exist in the selected vehicle: " + notAllowedFeature);
                        }
                );
            }
        } catch (ValidationException e) {
            errorMessage = e.getMessage();
        }

        return errorMessage;
    }
}
