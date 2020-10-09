package ekol.email.domain;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@Getter
@Setter
@Document(collection = "emails")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Email extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	private String source;
	private List<String> concern;
	private String from;
	private String replyTo;
	private String subject;
	private String body;
	private Boolean html;
	private EmailSendStatus sentStatus; 
	private List<String> to = new ArrayList<>();
	private List<String> cc = new ArrayList<>();
	private List<String> bcc = new ArrayList<>();
	private List<String> attachments = new ArrayList<>();
	private LocalDateTime sentTime;
	private String exceptionStackTrace;
	private String error;
}
