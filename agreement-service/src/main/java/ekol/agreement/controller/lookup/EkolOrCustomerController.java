package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.EkolOrCustomer;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/ekol-or-customer")
public class EkolOrCustomerController extends BaseEnumApiController<EkolOrCustomer> {

    @PostConstruct
    public void init(){
        setType(EkolOrCustomer.class);
    }
}
