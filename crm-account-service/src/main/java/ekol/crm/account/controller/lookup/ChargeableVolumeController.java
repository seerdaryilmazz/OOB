package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.ChargeableVolume;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/lookup/chargeable-volume")
public class ChargeableVolumeController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<ChargeableVolume> list() {
        return Arrays.asList(ChargeableVolume.values());
    }

    @RequestMapping(value="/{shipmentLoadingType}", method= RequestMethod.GET)
    public List<ChargeableVolume> list(@PathVariable String shipmentLoadingType) {

        return Arrays.stream(ChargeableVolume.values()).filter(chargeableVolume ->
                chargeableVolume.getShipmentLoadingType().name().equalsIgnoreCase(shipmentLoadingType))
                .collect(Collectors.toList());
    }
}
