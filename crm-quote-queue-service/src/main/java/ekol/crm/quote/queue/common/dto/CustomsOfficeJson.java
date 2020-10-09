package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class CustomsOfficeJson {
	private CountryJson country;
	private Set<ExternalSystemIdJson> externalIds;
}
