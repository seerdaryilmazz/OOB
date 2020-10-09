package ekol.crm.inbound.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ekol.crm.inbound.domain.*;
import ekol.crm.inbound.domain.dto.ProposeQuote;
import ekol.crm.inbound.service.*;
import ekol.exceptions.BadRequestException;
import ekol.model.*;
import lombok.AllArgsConstructor;

@Validated
@RestController
@RequestMapping("/inbound")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InboundController {
	
	private InboundService inboundService;
	private LookupService lookupService;
	private Environment environment;
	
	@GetMapping("/lookup/{lookup}")
	public Set<CodeNamePair> lookup(@PathVariable String lookup, @RequestParam(required = false) Map<String, String> parameters){
		return lookupService.list(lookup, parameters);
	}
	
	@PostMapping("/account")
	public Set<IdNamePair> findAccounts(@RequestBody Map<String, Object> body){
		String address = Optional.ofNullable(body.get("address")).map(String::valueOf).orElseThrow(BadRequestException::new);
		return inboundService.findAccounts(address);
	}
	
	@GetMapping("/go/{id}")
	public void go(@PathVariable String id, HttpServletResponse response) throws IOException {
		String url = new StringBuilder()
				.append(environment.getProperty("oneorder.gateway"))
				.append("/ui/crm/inbound-mail/")
				.append(id)
				.toString();
		response.sendRedirect(url);
	}
	
	@GetMapping("/propose-quote")
	public String proposeQupte(@Valid ProposeQuote proposeQuote) {
		return new StringBuilder()
				.append(environment.getProperty("oneorder.gateway"))
				.append("/ui/crm/quote/new/")
				.append(proposeQuote.getQuoteType().name())
				.append("/")
				.append(proposeQuote.getServiceArea())
				.append("/")
				.append(proposeQuote.getAccount())
				.append("?inbound=")
				.append(proposeQuote.getInbound())
				.toString();
	}
	
	@GetMapping("/{id}")
	public Inbound get(@PathVariable String id) {
		return inboundService.getInbound(id);
	}
	
	@PostMapping
	public Map<String, Object> save(@RequestBody Message message) {
		Inbound inbound = inboundService.createInbound(message);
		Map<String, Object> result = new HashMap<>();
		result.put("id", inbound.getId());
		return result;
	}
}
