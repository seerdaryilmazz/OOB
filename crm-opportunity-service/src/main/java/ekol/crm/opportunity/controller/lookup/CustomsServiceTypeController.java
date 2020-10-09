package ekol.crm.opportunity.controller.lookup;

import ekol.crm.opportunity.domain.enumaration.CustomsServiceType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@RestController
@RequestMapping("/lookup/customs-service-type")
public class CustomsServiceTypeController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<String> list() {
        List<String> response = new ArrayList<>();
        Arrays.stream(CustomsServiceType.values()).forEach(customsServiceType -> {
            response.addAll(customsServiceType.getOptions());
        });
        return response;
    }
}
