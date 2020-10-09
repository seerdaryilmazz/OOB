package ekol.crm.quote.queue.exportq.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "with")
@NoArgsConstructor
public class QuoteExportEventMessage {
	private String id;
}
