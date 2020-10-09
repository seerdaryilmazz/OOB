package ekol.crm.quote.queue.importq.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.quote.queue.importq.enums.Status;
import ekol.resource.controller.BaseEnumApiController;

@RestController
@RequestMapping("/import-queue/lookup/status")
public class ImportStatusLookup extends BaseEnumApiController<Status> {
	
	@PostConstruct
    public void init() {
        setType(Status.class);
    }
	
}
