package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;
import lombok.Data;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class WarehouseJson {
	private IdNamePair company;
	private IdNamePair companyLocation;
	private Set<ExternalSystemIdJson> externalIds;
}
