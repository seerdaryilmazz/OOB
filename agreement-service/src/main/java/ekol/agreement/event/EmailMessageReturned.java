package ekol.agreement.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.dto.EmailMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dogukan Sahinturk on 21.10.2019
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageReturned {

    private String from;
    private String replyTo;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String body;
    private Boolean html;
    private List<EmailMessage.Attachment> attachments;

}
