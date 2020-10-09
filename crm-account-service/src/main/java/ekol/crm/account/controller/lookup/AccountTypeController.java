package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.AccountType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/account-type")
public class AccountTypeController extends BaseEnumApiController<AccountType> {

    @PostConstruct
    public void init() {
        setType(AccountType.class);
    }
}
