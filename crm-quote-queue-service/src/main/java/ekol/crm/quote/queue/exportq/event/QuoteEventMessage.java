package ekol.crm.quote.queue.exportq.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class QuoteEventMessage {

	private Long quoteId;
}
