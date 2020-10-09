package ekol.crm.quote.queue.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteExportQueryJson {
	private Long quoteId;
	private Long quoteNumber;
}
