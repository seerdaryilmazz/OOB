package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.CalculationType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lookup/calculation-type")
public class CalculationTypeController {

    @RequestMapping(value="/{shipmentLoadingType}", method= RequestMethod.GET)
    public List<CalculationType> list(@PathVariable String shipmentLoadingType) {

        return Arrays.stream(CalculationType.values())
                .filter(calculationType -> calculationType.getShipmentLoadingType().equals(shipmentLoadingType))
                .collect(Collectors.toList());
    }
}
