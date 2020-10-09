package ekol.crm.quote.domain.dto.kartoteksservice;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyLocation {

    private Long id;
    private String name;
    private PostalAddress postaladdress;
    private Set<PhoneNumberWithType> phoneNumbers;
    private Set<CompanyLocationIdMapping> mappedIds;
    private Boolean active;

}
