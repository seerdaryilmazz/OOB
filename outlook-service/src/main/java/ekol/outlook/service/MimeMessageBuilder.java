package ekol.outlook.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;

import ekol.exceptions.BadRequestException;
import ekol.outlook.model.dto.*;

public class MimeMessageBuilder {
	private MimeMessageBuilder() {
		throw new UnsupportedOperationException();
	}
	
	public static MimeMessage build(MailJson mail) {
		try {
			MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(mail.getSender().getEmailAddress());
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getBody().getContent(), true);
			if(!CollectionUtils.isEmpty(mail.getAttachments())) {
				for (DocumentJson attachment : mail.getAttachments()) {
					ByteArrayResource resource = new ByteArrayResource(attachment.getBytes());
					helper.addAttachment(attachment.getDocumentName(), resource);
				}
			}
			Set<String> recipients = new HashSet<>(); 
			if(mail.isSendNoteToSender()){
				Optional.of(mail).map(MailJson::getSender).map(UserJson::getEmailAddress).ifPresent(recipients::add);
			}
			recipients.addAll(Optional.of(mail).map(MailJson::getInternalRecipients).map(Collection::stream).orElseGet(Stream::empty).map(UserJson::getEmailAddress).collect(Collectors.toSet()));
			recipients.addAll(Optional.of(mail).map(MailJson::getExternalRecipients).map(Collection::stream).orElseGet(Stream::empty).map(UserJson::getEmailAddress).collect(Collectors.toSet()));
			helper.setTo(recipients.stream().toArray(String[]::new));
			return message;
		} catch(MessagingException ex) {
			throw new BadRequestException("mail sending error", ex);
		}
	}
}
