package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.ChargeableWeight;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/lookup/chargeable-weight")
public class ChargeableWeightController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<ChargeableWeight> list() {
        return Arrays.asList(ChargeableWeight.values());
    }
}
