package ekol.email.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;

import ekol.email.client.FileServiceClient;
import ekol.email.domain.*;
import ekol.email.event.EmailEvent;
import ekol.email.repository.EmailRepository;
import ekol.exceptions.ResourceNotFoundException;
import ekol.model.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {
	
	private FileServiceClient fileService;
	private EmailRepository emailRepository;
	private MongoTemplate mongoTemplate;
	private ApplicationEventPublisher applicationEventPublisher;
	
	public Email sendMail(EmailMessage message) {
		Email email = message.toEntity();
		email.setAttachments(uploadAttachments(message));
		email.setSentStatus(EmailSendStatus.NOT_SENT);
		email = emailRepository.save(email);
		message.setId(email.getId());
		applicationEventPublisher.publishEvent(EmailEvent.with(message));
		return email;
	}
	
	public void sendMail(String id) {
		Email email = getByEmailId(id);
		
		EmailMessage message = new EmailMessage();
		message.setId(email.getId());
		message.setReplyTo(email.getReplyTo());
		message.setFrom(email.getFrom());
		message.setBcc(email.getBcc());
		message.setCc(email.getCc());
		message.setTo(email.getTo());
		message.setSubject(email.getSubject());
		message.setBody(email.getBody());
		message.setAttachments(email.getAttachments().stream().map(fileService::download).collect(Collectors.toList()));
		message.setHtml(email.getHtml());
		message.setConcern(email.getConcern());
		message.setSource("UNSPECIFIED".equals(email.getSource())?null:email.getSource());
		applicationEventPublisher.publishEvent(EmailEvent.with(message));
	}
	
	public Email getByEmailId(String id) {
		return Optional.ofNullable(emailRepository.findById(id)).orElseThrow(() -> new ResourceNotFoundException("Email with id {0} not found", id));
	}
	
	public Iterable<Email> getByEmailIds(String[] ids) {
		return emailRepository.findAll(Arrays.asList(ids));
	}
	
	public Email updateStatus(String id, EmailSendStatus status) {
		return updateStatus(id, status, null);
	}
	
	public Email updateStatus(String id, EmailSendStatus status, Throwable t) {
		Email email  = getByEmailId(id);
		email.setSentStatus(status);
		email.setExceptionStackTrace(Objects.nonNull(t) ? ExceptionUtils.getStackTrace(t): null);
		email.setError(Objects.nonNull(t) ? ExceptionUtils.getMessage(t): null);
		if(EmailSendStatus.SUCCESSFUL == status) {
			email.setSentTime(LocalDateTime.now());
		}
		return emailRepository.save(email);
	}
	
	private List<String> uploadAttachments(EmailMessage message){
		return Optional.of(message)
				.map(EmailMessage::getAttachments)
				.map(Collection::stream)
				.orElseGet(Stream::empty)
				.map(fileService::upload)
				.map(StringIdNamePair::getId)
				.collect(Collectors.toList());
	}
	
	public Iterable<CodeNamePair> listSources(){
		return ((List<String>)mongoTemplate.getCollection(mongoTemplate.getCollectionName(Email.class)).distinct("source"))
				.stream().sorted()
				.map(t->CodeNamePair.with(t, t))
				.collect(Collectors.toList());
	}
	
	public Iterable<CodeNamePair> listConcerns(String source){
		return ((List<String>)mongoTemplate.getCollection(mongoTemplate.getCollectionName(Email.class)).distinct("concern", new BasicDBObject("source", source)))
				.stream().sorted()
				.map(t->CodeNamePair.with(t, t))
				.collect(Collectors.toList());
	}
	
	public Page<Email> listBy(String source, String concern, EmailSendStatus sentStatus, Pageable page){
		Query query = new Query().with(page);
		if(Objects.nonNull(source)){
            query.addCriteria(Criteria.where("source").is(source));
        }
		if(Objects.nonNull(concern)){
			query.addCriteria(Criteria.where("concern").is(concern));
		}
		if(Objects.nonNull(sentStatus)){
			query.addCriteria(Criteria.where("sentStatus").is(sentStatus));
		}
		return PageableExecutionUtils.getPage(mongoTemplate.find(query, Email.class), page, ()-> mongoTemplate.count(query, Email.class));
	}

}
