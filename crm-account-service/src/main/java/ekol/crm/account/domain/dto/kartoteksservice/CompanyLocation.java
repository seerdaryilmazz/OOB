package ekol.crm.account.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Created by Dogukan Sahinturk on 3.01.2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyLocation {

    private Long id ;
    private String name;
    private String shortName;
    private PostalAddress postaladdress;
}
