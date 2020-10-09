package ekol.crm.quote.event;

import ekol.crm.quote.domain.dto.quote.QuoteJson;
import ekol.crm.quote.domain.model.quote.Quote;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName="with")
public class QuoteEvent {
	private QuoteJson previousData;
	private Quote data;
	private QuoteEventOperation operation;
	
	public static QuoteEvent with(Quote data, QuoteEventOperation operation) {
		return with(null, data, operation);
	}
}
