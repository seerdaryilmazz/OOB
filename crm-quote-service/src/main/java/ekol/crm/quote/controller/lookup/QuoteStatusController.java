package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.QuoteStatus;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/quote-status")
public class QuoteStatusController extends BaseEnumApiController<QuoteStatus> {

    @PostConstruct
    public void init() {
        setType(QuoteStatus.class);
    }
}
