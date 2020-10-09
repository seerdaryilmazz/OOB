package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.RiskFactor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lookup/risk-factor")
public class RiskFactorController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<String> list() {
        List<String> response = new ArrayList<>();
        Arrays.stream(RiskFactor.values()).forEach(riskFactor -> {
            response.addAll(riskFactor.getOptions());
        });
        return response;
    }

}
