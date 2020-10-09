package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName="with")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteIdMappingJson {
	private String application;
	private String applicationId;
}
