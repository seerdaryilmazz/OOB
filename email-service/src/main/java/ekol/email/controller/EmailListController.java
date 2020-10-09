package ekol.email.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.email.domain.*;
import ekol.email.service.EmailListService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor(onConstructor=@__(@Autowired))
@RequestMapping("/list")
public class EmailListController {
	
	private EmailListService emailListService;
	
	@GetMapping
	public Map<String, List<?>> list(){
		return emailListService.list();
	}
	
	@PostMapping("/blacklist")
	public EmailBlacklist addBlacklist(@RequestBody EmailBlacklist request) {
		return emailListService.addToBlacklist(request);
	}
	
	@PostMapping("/whitelist")
	public EmailWhitelist addWhitelist(@RequestBody EmailWhitelist request) {
		return emailListService.addToWhitelist(request);
	}
	
	@DeleteMapping("/blacklist/{id}")
	public void deleteBlacklist(@PathVariable String id) {
		emailListService.deleteBlacklist(id);
	}
	
	@DeleteMapping("/whitelist/{id}")
	public void deleteWhitelist(@PathVariable String id) {
		emailListService.deleteWhitelist(id);
	}
}
