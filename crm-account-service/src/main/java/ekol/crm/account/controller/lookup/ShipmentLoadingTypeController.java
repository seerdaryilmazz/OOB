package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.ShipmentLoadingType;
import ekol.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/lookup/shipment-loading-type")
public class ShipmentLoadingTypeController{

    @RequestMapping(value="/{serviceArea}", method= RequestMethod.GET)
    public List<ShipmentLoadingType> list(@PathVariable String serviceArea) {

        return Arrays.stream(ShipmentLoadingType.values()).filter(shipmentLoadingType ->
                shipmentLoadingType.getServiceAreas().contains(serviceArea))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/byCode", method = RequestMethod.GET)
    public ShipmentLoadingType findByCode(@RequestParam String code) {
        ShipmentLoadingType shipmentLoadingType = null;
        for (ShipmentLoadingType item : ShipmentLoadingType.values()) {
            if (item.getCode().equals(code)) {
                shipmentLoadingType = item;
                break;
            }
        }
        if (shipmentLoadingType != null) {
            return shipmentLoadingType;
        } else {
            throw new ResourceNotFoundException("Shipment loading type not found");
        }
    }
}
