package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.UnitOfMeasure;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lookup/unit-of-measure")
public class UnitOfMeasureController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<UnitOfMeasure> list(@RequestParam String scope) {

        return Arrays.stream(UnitOfMeasure.values())
                .filter(unitOfMeasure -> unitOfMeasure.getScope().contains(scope))
                .collect(Collectors.toList());
    }
}
