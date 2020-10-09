package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.CustomsClearanceType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lookup/customs-clearance-type")
public class CustomsClearanceTypeController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<CustomsClearanceType> list(@RequestParam String activity,
                                          @RequestParam String operation) {

        return Arrays.stream(CustomsClearanceType.values()).filter(customsClearance ->
                customsClearance.getScope().containsKey(operation) && customsClearance.getScope().get(operation).contains(activity))
                .collect(Collectors.toList());
    }
}
