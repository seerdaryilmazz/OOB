package ekol.kartoteks.controller.lookup;

import ekol.kartoteks.domain.ExportQueueStatus;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 12/12/2017.
 */
@RestController
@RequestMapping("/export-queue-status")
public class ExportQueueStatusController extends BaseEnumApiController<ExportQueueStatus> {

    @PostConstruct
    public void init() {
        setType(ExportQueueStatus.class);
    }
}