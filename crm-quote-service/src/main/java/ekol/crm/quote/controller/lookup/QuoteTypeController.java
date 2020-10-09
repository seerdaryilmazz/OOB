package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.QuoteType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/quote-type")
public class QuoteTypeController extends BaseEnumApiController<QuoteType> {

    @PostConstruct
    public void init() {
        setType(QuoteType.class);
    }
}
