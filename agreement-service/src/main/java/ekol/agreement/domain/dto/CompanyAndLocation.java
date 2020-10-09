package ekol.agreement.domain.dto;

import ekol.agreement.domain.dto.kartoteks.Company;
import ekol.agreement.domain.dto.kartoteks.CompanyLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dogukan Sahinturk on 11.10.2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName="with")
public class CompanyAndLocation {
    private Company company;
    private CompanyLocation location;
}
