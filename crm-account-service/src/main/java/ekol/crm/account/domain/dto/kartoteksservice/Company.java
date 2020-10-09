package ekol.crm.account.domain.dto.kartoteksservice;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    private Long id;

    private String name;

    private Country country;

    private Set<CompanySector> sectors;
    
    private Set<Contact> companyContacts;

    public CompanySector getDefaultCompanySector() {
        if (CollectionUtils.isNotEmpty(getSectors())) {
            for (CompanySector sector : getSectors()) {
                if (sector.isDefault()) {
                    return sector;
                }
            }
        }
        return null;
    }
}
