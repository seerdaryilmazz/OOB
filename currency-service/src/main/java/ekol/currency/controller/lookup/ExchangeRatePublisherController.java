package ekol.currency.controller.lookup;


import ekol.currency.domain.ExchangeRatePublisher;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/exchange-rate-publisher")
public class ExchangeRatePublisherController extends BaseEnumApiController<ExchangeRatePublisher> {

    @PostConstruct
    public void init() {
        setType(ExchangeRatePublisher.class);
    }
}
