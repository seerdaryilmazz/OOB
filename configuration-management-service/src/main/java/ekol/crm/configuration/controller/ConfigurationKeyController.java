package ekol.crm.configuration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.configuration.domain.ConfigurationKey;
import ekol.crm.configuration.service.ConfigurationKeyService;
import ekol.event.auth.Authorize;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/configuration-key")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationKeyController {
	
	private ConfigurationKeyService configurationKeyService;
	
	@GetMapping
	public List<ConfigurationKey> findAll() {
		return configurationKeyService.findAll();
	}
	
	@GetMapping("/{id}")
	public ConfigurationKey find(@PathVariable String id) {
		return configurationKeyService.findById(id);
	}
	
	@PostMapping
	@Authorize(operations = {"configuration-management.admin", "configuration-management.supervisor", "configuration-management.user"})
	public ConfigurationKey save(@RequestBody ConfigurationKey request) {
		return configurationKeyService.save(request);
	}
	
	@PutMapping("/{id}")
	@Authorize(operations = {"configuration-management.admin", "configuration-management.supervisor", "configuration-management.user"})
	public ConfigurationKey update(@PathVariable String id, @RequestBody ConfigurationKey request) {
		return configurationKeyService.update(id, request);
	}
	
}
