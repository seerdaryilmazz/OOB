package ekol.crm.quote.queue.importq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.queue.importq.enums.QuoteOrderRelation;
import ekol.model.CodeNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportQuoteOrderJson {
	private Long quoteCode;
	private String orderNumber;
	private CodeNamePair orderStatus;
	private QuoteOrderRelation relation;
}
