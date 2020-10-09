package ekol.notification.event.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage {
	private String source;
	private List<String> concern;
    private List<String> to;
    private String subject;
    private String body;
    private Boolean html;
}
