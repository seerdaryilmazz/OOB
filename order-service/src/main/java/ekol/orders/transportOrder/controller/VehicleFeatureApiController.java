package ekol.orders.transportOrder.controller;


import ekol.orders.transportOrder.domain.VehicleFeature;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by burak on 25/01/17.
 */
@RestController
@RequestMapping("/lookup/vehicle-features")
public class VehicleFeatureApiController extends BaseEnumApiController<VehicleFeature> {
    @PostConstruct
    public void init() {
        setType(VehicleFeature.class);
    }

    @RequestMapping(value = {"/filter/carrier"}, method = RequestMethod.GET)
    public List<VehicleFeature> getFeaturesForCarrier(){
        return Arrays.asList(
        		VehicleFeature.TRAILER, 
        		VehicleFeature.CONTAINER, 
        		VehicleFeature.BOX_BODY,
                VehicleFeature.CURTAIN_SIDER, 
                VehicleFeature.SLIDING_ROOF, 
                VehicleFeature.XL_CERTIFICATE, 
                VehicleFeature.MEGA,
                VehicleFeature.TAIL_LIFT, 
                VehicleFeature.SWAP_BODY, 
                VehicleFeature.SECURITY_SENSOR
            );
    }

    @RequestMapping(value = {"/filter/create-order"}, method = RequestMethod.GET)
    public List<VehicleFeature> getFeaturesForCreateOrder(){
        return Arrays.asList(
        		VehicleFeature.CURTAIN_SIDER,
                VehicleFeature.BOX_BODY,
                VehicleFeature.MEGA,
                VehicleFeature.XL_CERTIFICATE,
                VehicleFeature.DOUBLE_DECK,
                VehicleFeature.LIFTING_ROOF,
                VehicleFeature.SLIDING_ROOF,
                VehicleFeature.SECURITY_SENSOR,
                VehicleFeature.TAIL_LIFT,
                VehicleFeature.TRAILER,
                VehicleFeature.CONTAINER,
                VehicleFeature.SWAP_BODY
            );
    }

}
