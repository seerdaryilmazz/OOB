package ekol.crm.configuration.controller;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.configuration.domain.Option;
import ekol.crm.configuration.service.OptionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/option")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OptionController {
	
	private OptionService optionService;

	@GetMapping
	public Collection<Option> listValue(
			@RequestParam(required = false) String key, 
			@RequestParam(required = false) Long subsidiaryId) {
		if(StringUtils.isNotBlank(key) && Objects.nonNull(subsidiaryId)) {
			return Arrays.asList(optionService.get(key, subsidiaryId));
		} else if(StringUtils.isNotBlank(key)) {
			return optionService.list(key);
		} else if(Objects.nonNull(subsidiaryId)) {
			return optionService.list(subsidiaryId);
		}
		return optionService.list();
	}
	
	@GetMapping("/{key}")
	public Option getValue(
			@PathVariable String key, 
			@RequestParam Long subsidiaryId) {
		return optionService.get(key, subsidiaryId);
	}
	
}
