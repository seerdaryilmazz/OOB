package ekol.crm.opportunity.event.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.opportunity.domain.dto.OpportunityJson;
import ekol.model.IdNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountMerge {
	
	private IdNamePair deletedAccount;
	private IdNamePair account;
	private List<OpportunityJson> opportunities;
	

}
