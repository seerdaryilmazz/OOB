package ekol.crm.quote.queue.importq.event;

import ekol.crm.quote.queue.importq.dto.ImportQuoteOrderJson;
import lombok.*;

@Data
@AllArgsConstructor(staticName = "with")
public class ImportQuoteOrderEvent {
	private ImportQuoteOrderJson data;
}
