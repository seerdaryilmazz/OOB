package ekol.email.service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.*;
import org.springframework.retry.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import ekol.email.config.JavaMailSenderConfig;
import ekol.email.domain.*;
import ekol.email.exception.ExportFailureException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmailTransportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTransportService.class);

	private String getPrefix() {
		return isProd() ? "" : "[[" + Stream.of(environment.getActiveProfiles()).findFirst().map(String::toUpperCase).orElse("") + "]] - ";
	}

	private boolean isProd() {
		return isEnvironment("prod");
	}
	
	private boolean isEnvironment(String env) {
		return Stream.of(environment.getActiveProfiles()).anyMatch(env::equalsIgnoreCase);
	}
	
	private boolean isEligable(String email) {
		return isEnvironment("local") || emailListService.isEligable(email);
	}

	private EmailService emailService;
	private EmailListService emailListService;
	private Environment environment;
	private JavaMailSender defaultMailSender;
	private JavaMailSenderConfig javaMailSenderConfig;


	@Transactional
	@Retryable(	value= ExportFailureException.class,
				maxAttemptsExpression="${oneorder.consecutiveErrorThreshold:3}", 
				backoff=@Backoff( delayExpression="${oneorder.backOff.delay:360000}", multiplierExpression="${oneorder.backOff.multiplier:2}"))
	public void transport(EmailMessage emailMessage) {

		String[] to = Optional.ofNullable(emailMessage.getTo()).map(Collection::stream).orElseGet(Stream::empty).filter(this::isEligable).toArray(String[]::new);
		String[] cc = Optional.ofNullable(emailMessage.getCc()).map(Collection::stream).orElseGet(Stream::empty).filter(this::isEligable).toArray(String[]::new);
		String[] bcc = Optional.ofNullable(emailMessage.getBcc()).map(Collection::stream).orElseGet(Stream::empty).filter(this::isEligable).toArray(String[]::new);

		if(ArrayUtils.isEmpty(to) && ArrayUtils.isEmpty(cc) && ArrayUtils.isEmpty(bcc)) {
			return;
		}

		try {
			MimeMessage mimeMessage = defaultMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			helper.setFrom(javaMailSenderConfig.getUsername());
			if (emailMessage.getReplyTo() != null) {
				helper.setReplyTo(emailMessage.getReplyTo());
			}

			if (ArrayUtils.isNotEmpty(to)) {
				helper.setTo(to);
			}
			if (ArrayUtils.isNotEmpty(cc)) {
				helper.setCc(cc);
			}
			if (ArrayUtils.isNotEmpty(bcc)) {
				helper.setBcc(bcc);
			}
			String subject = StringUtils.prependIfMissingIgnoreCase(emailMessage.getSubject(), getPrefix());
			if (subject != null) {
				helper.setSubject(subject.trim());
			}
			if (emailMessage.getBody() != null) {
				helper.setText(emailMessage.getBody(), emailMessage.getHtml());
			} else {
				helper.setText("", emailMessage.getHtml());
			}
			if (!CollectionUtils.isEmpty(emailMessage.getAttachments())) {
				for (EmailMessage.Attachment attachment : emailMessage.getAttachments()) {
					ByteArrayResource source = new ByteArrayResource(Base64.decodeBase64(attachment.getBase64EncodedContent()));
					helper.addAttachment(attachment.getName(), source);
				}
			}

			defaultMailSender.send(mimeMessage);
			emailService.updateStatus(emailMessage.getId(), EmailSendStatus.SUCCESSFUL);
		} catch (MessagingException | MailException e ) {
			LOGGER.warn("Mail transport failure, id: {}", emailMessage.getId());
			throw new ExportFailureException(MessageFormat.format("Mail transport failure, id: {0}", emailMessage.getId()), e);
		} 

	}
	@Recover
	public void recoverTransport(ExportFailureException throwable, EmailMessage emailMessage) {
		LOGGER.warn("Mail transport consecutive failure, id: {}", emailMessage.getId());
		emailService.updateStatus(emailMessage.getId(), EmailSendStatus.FAILED, throwable.getCause());
	}
}
