package ekol.location.controller;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.ZoneZipCodeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by ozer on 14/12/16.
 */
@RestController
@RequestMapping("/lookup/zone-zip-code-type")
public class ZoneZipCodeTypeController extends BaseEnumApiController<ZoneZipCodeType> {

    @PostConstruct
    public void init() {
        setType(ZoneZipCodeType.class);
    }
}
