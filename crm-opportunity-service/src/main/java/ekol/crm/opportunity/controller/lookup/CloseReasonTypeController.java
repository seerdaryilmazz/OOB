package ekol.crm.opportunity.controller.lookup;

import ekol.crm.opportunity.domain.enumaration.CloseReasonType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by Dogukan Sahinturk on 29.11.2019
 */
@RestController
@RequestMapping("/lookup/close-reason-type")
public class CloseReasonTypeController extends BaseEnumApiController<CloseReasonType> {
    @PostConstruct
    public void init(){
        setType(CloseReasonType.class);
    }
}
