package ekol.billingitem.controller.lookup;

import ekol.billingitem.domain.BillingItemType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/billing-item-type")
public class BillingItemTypeController extends BaseEnumApiController<BillingItemType> {

    @PostConstruct
    public void init() {
        setType(BillingItemType.class);
    }
}
