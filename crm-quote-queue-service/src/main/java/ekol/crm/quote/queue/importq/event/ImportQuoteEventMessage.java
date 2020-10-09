package ekol.crm.quote.queue.importq.event;

import ekol.crm.quote.queue.importq.dto.ImportQuoteJson;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class ImportQuoteEventMessage {
	private String id;
	private ImportQuoteJson data;
}
