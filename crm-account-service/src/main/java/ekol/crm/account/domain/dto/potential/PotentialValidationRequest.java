package ekol.crm.account.domain.dto.potential;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PotentialValidationRequest {
    private Long potentialId;
    private String shipmentLoadingType;
    private String serviceArea;
    private BigDecimal payWeight;
    private Set<CodeNamePair> services;
    
    
}
