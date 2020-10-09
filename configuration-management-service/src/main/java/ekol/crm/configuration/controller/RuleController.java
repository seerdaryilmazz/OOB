package ekol.crm.configuration.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.configuration.service.RuleService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/rule")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RuleController {

	private RuleService ruleService;
	
	@PostMapping("/evaluate")
	public Object evaluate(
			@RequestBody Map<String, Object> facts,
			@RequestParam(required = false) String code, 
			@RequestParam(required = false) Long subsidiaryId) throws IOException {
		return ruleService.evaluate(code, subsidiaryId, facts);
	}
}
