package ekol.email.domain;

import java.util.*;
import java.util.stream.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage {

	private String source;
	private List<String> concern;
	private String id;      
    private String from;
    private String replyTo;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String body;
    private Boolean html;
    private List<Attachment> attachments;
    
    public Email toEntity() {
    	Email entity = new Email();
    	entity.setSource(Optional.ofNullable(getSource()).orElse("UNSPECIFIED"));
    	entity.setConcern(getConcern());
    	entity.setId(getId());
    	entity.setFrom(getFrom());
    	entity.setReplyTo(getReplyTo());
    	entity.setSubject(getSubject());
    	entity.setBody(getBody());
    	entity.setHtml(getHtml());
    	entity.setTo(Optional.ofNullable(getTo()).map(Collection::stream).orElseGet(Stream::empty).collect(Collectors.toList()));
    	entity.setCc(Optional.ofNullable(getCc()).map(Collection::stream).orElseGet(Stream::empty).collect(Collectors.toList()));
    	entity.setBcc(Optional.ofNullable(getBcc()).map(Collection::stream).orElseGet(Stream::empty).collect(Collectors.toList()));
    	return entity;
    	
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attachment {
        private String name;
        private String base64EncodedContent;
    }
}
