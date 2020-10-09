package ekol.crm.inbound.domain.dto;

import java.util.Objects;

import ekol.model.IdNamePair;
import lombok.*;

@Data
@AllArgsConstructor(staticName = "with")
public class CompanyAccount {
	private IdNamePair company;
	private IdNamePair account;
	
	public boolean isEligable() {
		return Objects.nonNull(company) && Objects.nonNull(account);
	}
}
