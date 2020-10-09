package ekol.currency.controller.lookup;


import ekol.currency.domain.Currency;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/currency")
public class CurrencyController extends BaseEnumApiController<Currency> {

    @PostConstruct
    public void init() {
        setType(Currency.class);
    }
}
