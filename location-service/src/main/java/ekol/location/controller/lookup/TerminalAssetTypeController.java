package ekol.location.controller.lookup;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.location.enumeration.TerminalAssetType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 28/04/2017.
 */
@RestController
@RequestMapping("/lookup/terminal-asset-type")
public class TerminalAssetTypeController extends BaseEnumApiController<TerminalAssetType> {

    @PostConstruct
    public void init() {
        setType(TerminalAssetType.class);
    }
}
