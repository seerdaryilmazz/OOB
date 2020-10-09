package ekol.kartoteks.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateShortNameRequest {

    private Long companyId;
    private String shortName;
    private Map<Long, String> locationShortNames = new HashMap<>();


    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Map<Long, String> getLocationShortNames() {
        return locationShortNames;
    }

    public void setLocationShortNames(Map<Long, String> locationShortNames) {
        this.locationShortNames = locationShortNames;
    }
}
