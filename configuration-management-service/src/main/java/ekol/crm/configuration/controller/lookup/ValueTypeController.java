package ekol.crm.configuration.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.*;

import ekol.crm.configuration.domain.ValueType;
import ekol.resource.controller.BaseEnumApiController;

@RestController
@RequestMapping("/lookup/value-type")
public class ValueTypeController extends BaseEnumApiController<ValueType> {
	
	@PostConstruct
	private void init() {
		setType(ValueType.class);
	}
}
