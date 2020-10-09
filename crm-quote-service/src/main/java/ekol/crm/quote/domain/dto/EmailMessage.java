package ekol.crm.quote.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage {

	private String source;
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
