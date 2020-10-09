package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.model.Agreement;
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
public class AgreementJson {
	
    private Long id;
    private AccountJson account;

    public Agreement toEntity() {
    	return Agreement.builder()
    			.id(getId())
    			.build();
    			
    }
    
    public static AgreementJson fromEntity(Agreement agreement) {
    	return new AgreementJsonBuilder()
    			.id(agreement.getId())
    			.account(AccountJson.fromEntity(agreement.getAccount()))
    			.build();
    }

}
