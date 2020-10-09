package ekol.location.controller;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.RegionCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/region-category")
public class RegionCategoryController extends BaseEnumApiController<RegionCategory> {

    @PostConstruct
    public void init() {
        setType(RegionCategory.class);
    }
}
