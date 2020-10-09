package ekol.notification.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.*;

import ekol.notification.domain.TemplateType;
import ekol.resource.controller.BaseEnumApiController;

@RestController
@RequestMapping("/lookup/template-type")
public class TemplateTypeLookup extends BaseEnumApiController<TemplateType> {

	@PostConstruct
	private void init() {
		setType(TemplateType.class);
	}
}
