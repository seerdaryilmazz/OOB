package ekol.kartoteks.controller.lookup;

import ekol.resource.controller.BaseEnumApiController;
import ekol.kartoteks.domain.ImportQueueStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 11/01/2017.
 */
@RestController
@RequestMapping("/import-queue-status")
public class ImportQueueStatusController extends BaseEnumApiController<ImportQueueStatus> {

    @PostConstruct
    public void init() {
        setType(ImportQueueStatus.class);
    }
}
