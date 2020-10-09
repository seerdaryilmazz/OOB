package ekol.crm.opportunity.controller.lookup;

import ekol.crm.opportunity.domain.enumaration.FrequencyType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by Dogukan Sahinturk on 21.11.2019
 */
@RestController
@RequestMapping("/lookup/frequency-type")
public class FrequencyTypeController extends BaseEnumApiController<FrequencyType> {
    @PostConstruct
    public void init(){
        setType(FrequencyType.class);
    }
}
