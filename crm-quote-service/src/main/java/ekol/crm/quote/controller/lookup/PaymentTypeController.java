package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.PaymentType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/payment-type")
public class PaymentTypeController extends BaseEnumApiController<PaymentType> {

    @PostConstruct
    public void init() {
        setType(PaymentType.class);
    }
}
