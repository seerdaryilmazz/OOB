package ekol.crm.quote.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    private Long id;

    private String name;

    private Country country;

    private Set<CompanyLocation> companyLocations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<CompanyLocation> getCompanyLocations() {
        return companyLocations;
    }

    public void setCompanyLocations(Set<CompanyLocation> companyLocations) {
        this.companyLocations = companyLocations;
    }
}
