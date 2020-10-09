package ekol.crm.quote.queue.common.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyJson {
	private Long id;
	private String name;
	private Set<IdNamePair> companyLocations;
}
