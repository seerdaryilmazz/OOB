package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.model.Quote;
import ekol.model.CodeNamePair;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteJson {
	
    private Long id;
    private AccountJson account;
    private CodeNamePair status;
	
}
