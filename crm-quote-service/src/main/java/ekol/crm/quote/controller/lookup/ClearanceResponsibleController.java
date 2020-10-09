package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.ClearanceResponsible;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/lookup/clearance-responsible")
public class ClearanceResponsibleController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<ClearanceResponsible> list(@RequestParam String activity,
                                           @RequestParam String operation,
                                           @RequestParam(required = false) String incoterm) {

        return Arrays.stream(ClearanceResponsible.values()).filter(clearanceResponsible ->
                clearanceResponsible.isSuitable(activity, operation, incoterm))
                .collect((Collectors.toList()));
    }
}
