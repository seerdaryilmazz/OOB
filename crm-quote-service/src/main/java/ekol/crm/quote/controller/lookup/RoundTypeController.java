package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.RoundType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lookup/round-type")
public class RoundTypeController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<RoundType> list() {
        return Arrays.asList(RoundType.values());
    }
}
