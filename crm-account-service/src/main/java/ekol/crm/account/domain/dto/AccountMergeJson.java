package ekol.crm.account.domain.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.dto.potential.PotentialJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AccountMergeJson {
	
	private AccountJson deletedAccount;
    private AccountJson account;
    private List<PotentialJson> potentials = new ArrayList<>();
    private List<QuoteJson> quotes = new ArrayList<>();
    private List <AgreementJson> agreements = new ArrayList<>();
    private List<ActivityJson> activities = new ArrayList<>();
    private List <OpportunityJson> opportunities = new ArrayList<>();
    
}
