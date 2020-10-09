package ekol.crm.inbound.event.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.mongodb.domain.datetime.UtcDateTime;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class QuoteJson {
	private Long id;
	private String name;
	private UtcDateTime createdAt;
	private String createdBy;
	private Map<String, String> quoteAttribute = new HashMap<>();
}
