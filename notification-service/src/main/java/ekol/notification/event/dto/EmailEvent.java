package ekol.notification.event.dto;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailEvent {
	private String source;
	private List<String> concern;
    private List<String> to;
    private String subject;
    private String body;
    private Boolean html;
}
