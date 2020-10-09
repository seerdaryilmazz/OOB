package ekol.email.controller;

import java.util.LinkedHashSet;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import ekol.email.domain.*;
import ekol.email.service.EmailService;
import ekol.model.CodeNamePair;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class EmailController {
	
	private EmailService emailService;
	
	@GetMapping("/lookup/source")
	public Iterable<CodeNamePair> listSources() {
		return emailService.listSources();
	}
	
	@GetMapping("/lookup/concern")
	public Iterable<CodeNamePair> listConcerns(@RequestParam String source) {
		return emailService.listConcerns(source);
	}
	
	@GetMapping("/lookup/sent-status")
	public Iterable<EmailSendStatus> listSentStatus() {
		return Stream.of(EmailSendStatus.values()).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@PostMapping("/send")
	public String sendMail(@RequestBody EmailMessage emailMessage) {
		return emailService.sendMail(emailMessage).getId();
	}

	@GetMapping("/{id}")
	public Email getMail(@PathVariable String id) {
		return emailService.getByEmailId(id);
	}
	
	@GetMapping("/{id}/send")
	public void sendMail(@PathVariable String id) {
		emailService.sendMail(id);
	}
	
	@GetMapping("/list-by-id")
	public Iterable<Email> getMails(@RequestParam String ids) {
		return emailService.getByEmailIds(ids.split(","));
	}
	
	@GetMapping("/list-by")
	public Page<Email> getMails(@RequestParam(required = false) String source, @RequestParam(required = false) String concern, EmailSendStatus sentStatus, @RequestParam(required = false, defaultValue = "0") Integer page) {
		return emailService.listBy(source, concern, sentStatus, new PageRequest(page, 20, Sort.Direction.DESC, "sentTime","createdAt"));
	}
	
	
}