package ekol.crm.quote.queue.exportq.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.quote.queue.exportq.enums.Status;
import ekol.resource.controller.BaseEnumApiController;

@RestController
@RequestMapping("/export-queue/lookup/status")
public class ExportStatusLookup extends BaseEnumApiController<Status> {
	
	@PostConstruct
    public void init() {
        setType(Status.class);
    }
	
}
