package ekol.crm.quote.queue.importq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportQuoteJson {
	private String externalSystemName;
	private String externalSystemCode;
	private Long quoteCode;
}
