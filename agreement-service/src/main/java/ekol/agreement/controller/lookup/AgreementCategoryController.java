package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.AgreementCategory;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/agreement-category")
public class AgreementCategoryController extends BaseEnumApiController<AgreementCategory> {

    @PostConstruct
    public void init() {
        setType(AgreementCategory.class);
    }
}
