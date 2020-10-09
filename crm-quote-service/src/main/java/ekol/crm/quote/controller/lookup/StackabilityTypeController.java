package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.StackabilityType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/lookup/stackability-type")
public class StackabilityTypeController{

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<String> list() {
        List<String> response = new ArrayList<>();
        Arrays.stream(StackabilityType.values()).forEach(stackabilityType -> {
            response.addAll(stackabilityType.getOptions());
        });
        return response;
    }
}
