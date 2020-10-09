package ekol.email.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import ekol.email.domain.*;
import ekol.email.repository.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmailListService {
	
	private EmailBlacklistRepository blacklistRepository;
	private EmailWhitelistRepository whitelistRepository;
	
	public EmailBlacklist addToBlacklist(EmailBlacklist item) {
		return blacklistRepository.save(item);
	}
	
	public EmailWhitelist addToWhitelist(EmailWhitelist item) {
		return whitelistRepository.save(item);
	}
	
	public void deleteBlacklist(String id) {
		blacklistRepository.delete(id);
	}
	
	public void deleteWhitelist(String id) {
		whitelistRepository.delete(id);
	}
	
	public Map<String, List<?>> list(){
		Map<String, List<?>> result = new LinkedHashMap<>();
		result.put("blacklist", blacklistRepository.findAll());
		result.put("whitelist", whitelistRepository.findAll());
		return result;
	}
	
	public boolean isEligable(String email) {
		Example<EmailBlacklist> blacklistEmail = Example.of(EmailBlacklist.with(null, email), ExampleMatcher.matchingAny());
		Example<EmailWhitelist> whitelistEmail = Example.of(EmailWhitelist.with(null, email), ExampleMatcher.matchingAny());
		
		return !blacklistRepository.exists(blacklistEmail) && (0 == whitelistRepository.count() || whitelistRepository.exists(whitelistEmail));
	}
}
