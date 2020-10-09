package ekol.location.controller.lookup;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.location.enumeration.PortAssetType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by burak on 11/04/17.
 */
@RestController
@RequestMapping("/lookup/port-asset-type")
public class PortAssetTypeController extends BaseEnumApiController<PortAssetType> {

    @PostConstruct
    public void init() {
        setType(PortAssetType.class);
    }
}
