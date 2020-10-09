package ekol.crm.inbound.service;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import ekol.crm.inbound.client.*;
import ekol.crm.inbound.domain.*;
import ekol.crm.inbound.domain.dto.Document;
import ekol.crm.inbound.event.dto.QuoteJson;
import ekol.crm.inbound.repository.InboundRepository;
import ekol.exceptions.ResourceNotFoundException;
import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InboundService {
	
	private static final String REGEX = "(?<name>[\\w.]+)\\@(?<domain>\\w+\\.\\w+)(\\.\\w+)?";
	
	private InboundRepository inboundRepository;
	private KartoteksServiceClient kartoteksServiceClient;
	private AccountServiceClient accountServiceClient;
	private UserServiceClient userServiceClient;
	private FileServiceClient fileServiceClient;
	private QuoteServiceClient quoteServiceClient;
	
	public Inbound getInbound(String id) {
		return Optional.ofNullable(inboundRepository.findOne(id)).orElseThrow(ResourceNotFoundException::new);
	}
	
	@Transactional
	public Inbound createInbound(Message message) {
		Inbound entitiy = Optional.ofNullable(inboundRepository.findByMessageId(message.getId())).orElseGet(Inbound::new);
		if(Objects.isNull(entitiy.getId())) {
			message.setFrom(extractAddress(message.getFrom()));
			entitiy.setMessage(message);
			entitiy.setOwner(userServiceClient.searchUser(message.getUserEmail()));
			message.setAttachments(message.getAttachments().stream().map(fileServiceClient::upload).collect(Collectors.toList()));
			return inboundRepository.save(entitiy);
		} 
		return entitiy;
	}
	
	@Transactional
	public void relateInbound(QuoteJson quote) {
		String inboundId = Optional.ofNullable(quote.getQuoteAttribute()).orElseGet(Collections::emptyMap).get("inbound");
		if(StringUtils.isBlank(inboundId)) {
			return;
		}
		Inbound inbound = inboundRepository.findOne(inboundId);
		if(Objects.isNull(inbound)) {
			return;
		}
		if(inbound.getQuotes().stream().map(Quote::getId).anyMatch(quote.getId()::equals)) {
			return;
		}
		inbound.getQuotes().add(new Quote(quote.getId(), quote.getName(), quote.getCreatedAt().getDateTime(), quote.getCreatedBy()));
		inboundRepository.save(inbound);
		
		if(!CollectionUtils.isEmpty(inbound.getMessage().getAttachments())) {
			List<Document> documents = inbound.getMessage().getAttachments().stream().map(a->new Document(a.getId(), a.getName(), true)).collect(Collectors.toList());
			quoteServiceClient.updateDocuments(quote.getId(), documents);
		}
	}
	
	public Set<IdNamePair> findAccounts(String address) {
		Set<IdNamePair> companies = kartoteksServiceClient.findCompanyByEmail(address);
		if(1 != companies.size()) {
			companies.addAll(kartoteksServiceClient.findCompanyByDomain(address));
		}
		return companies.stream().map(IdNamePair::getId).distinct().map(accountServiceClient::findAccountByCompanyIdIgnoreException).filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	private String extractAddress(String address) {
		final Pattern pattern = Pattern.compile(REGEX);
		final Matcher matcher = pattern.matcher(address);
		while (matcher.find()) {
			String found = matcher.group(0);
			if(StringUtils.isNotBlank(found)) {
				return found;
			}
		}
		return address;
	}
}
