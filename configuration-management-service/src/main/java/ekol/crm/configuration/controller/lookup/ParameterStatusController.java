package ekol.crm.configuration.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.*;

import ekol.crm.configuration.domain.ParameterStatus;
import ekol.resource.controller.BaseEnumApiController;

@RestController
@RequestMapping("/lookup/parameter-status")
public class ParameterStatusController extends BaseEnumApiController<ParameterStatus> {
	
	@PostConstruct
	private void init() {
		setType(ParameterStatus.class);
	}
}
