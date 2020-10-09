package ekol.crm.quote.event.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class AccountMerge {
	private IdNamePair account;
	private List<IdNamePair> quotes;
}
