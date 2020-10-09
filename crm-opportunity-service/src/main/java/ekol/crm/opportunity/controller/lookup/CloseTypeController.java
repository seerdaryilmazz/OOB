package ekol.crm.opportunity.controller.lookup;

import ekol.crm.opportunity.domain.enumaration.CloseType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by Dogukan Sahinturk on 8.01.2020
 */
@RestController
@RequestMapping("/lookup/close-type")
public class CloseTypeController extends BaseEnumApiController<CloseType> {
    @PostConstruct
    public void init(){
        setType(CloseType.class);
    }
}
