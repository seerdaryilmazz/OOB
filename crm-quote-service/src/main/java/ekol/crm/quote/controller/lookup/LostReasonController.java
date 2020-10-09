package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.LostReasonType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lookup/lost-reason")
public class LostReasonController {

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<LostReasonType> list() {
        return Arrays.asList(LostReasonType.values());
    }

}
