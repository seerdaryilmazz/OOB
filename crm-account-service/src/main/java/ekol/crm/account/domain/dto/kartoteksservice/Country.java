package ekol.crm.account.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    private Long id;

    private String countryName;

    private String iso;

    private boolean euMember;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public boolean isEuMember() {
        return euMember;
    }

    public void setEuMember(boolean euMember) {
        this.euMember = euMember;
    }
}
