package ekol.crm.account.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class QuoteEventMessage {

	private Long quoteId;
	private Long accountId;
}
