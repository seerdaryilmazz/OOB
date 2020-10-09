package ekol.agreement.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
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

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attachment {
        private String name;
        private String base64EncodedContent;
    }
}
