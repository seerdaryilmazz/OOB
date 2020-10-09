package ekol.crm.quote.domain.dto;

import ekol.crm.quote.domain.dto.kartoteksservice.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName="with")
public class CompanyAndLocation {
    private Company company;
    private CompanyLocation location;
    
    public boolean isRelated() {
    	return company.getCompanyLocations().parallelStream().map(CompanyLocation::getId).anyMatch(location.getId()::equals);
    }
}
