package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.StampTaxPayer;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/stamp-tax-payer")
public class StampTaxPayerController extends BaseEnumApiController<StampTaxPayer> {

    @PostConstruct
    public void init() {
        setType(StampTaxPayer.class);
    }
}
