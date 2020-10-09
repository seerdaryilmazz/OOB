package ekol.crm.configuration.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.configuration.domain.Configuration;
import ekol.crm.configuration.service.ConfigurationService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/configuration")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationController {
	
	private ConfigurationService configurationService;
	
	@PostConstruct
	@GetMapping("/clear-cache")
	public String clearCache() {
		return configurationService.clearCache();
	}
	
	@GetMapping
	public List<Configuration> findAll(@RequestParam(required = false) Long subsidiaryId) {
		return Optional.ofNullable(subsidiaryId)
				.map(configurationService::findBySubsidiary)
				.orElseGet(configurationService::findAll);
	}
	
	@GetMapping("/default")
	public List<Configuration> findDefaults(){
		return configurationService.findDefaults();
	}
	
	@GetMapping("/default/{key}")
	public Configuration findDefaults(@PathVariable String key){
		return configurationService.findDefaults().parallelStream().filter(t->key.equalsIgnoreCase(t.getKey().getCode())).findFirst().orElse(null);
	}
	
	@GetMapping("/{id}")
	public Configuration find(@PathVariable String id) {
		return configurationService.findById(id);
	}
	
	@PostMapping("/bulk")
	public List<Configuration> save(@RequestBody Collection<Configuration> request) {
		return request.stream().map(configurationService::upsert).filter(Objects::nonNull).collect(Collectors.toCollection(LinkedList::new));
	}
	
	@PostMapping
	public Configuration save(@RequestBody Configuration request) {
		return configurationService.save(request);
	}
	
	@PutMapping("/{id}")
	public Configuration update(@PathVariable String id, @RequestBody Configuration request) {
		return configurationService.update(id, request);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		configurationService.delete(id);
	}
}
