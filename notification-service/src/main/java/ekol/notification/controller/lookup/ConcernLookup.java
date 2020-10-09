package ekol.notification.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.*;

import ekol.notification.domain.Concern;
import ekol.resource.controller.BaseEnumApiController;

@RestController
@RequestMapping("/lookup/concern")
public class ConcernLookup extends BaseEnumApiController<Concern> {

	@PostConstruct
	private void init() {
		setType(Concern.class);
	}
}
