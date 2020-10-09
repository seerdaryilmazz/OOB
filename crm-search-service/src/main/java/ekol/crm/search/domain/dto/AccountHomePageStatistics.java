package ekol.crm.search.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountHomePageStatistics {
	
	private Long totalCount;
	private List<AccountTypeBasedCount> accountTypeBasedCounts;
}
