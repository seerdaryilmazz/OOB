package ekol.crm.quote.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForceCompanyExportEventMessageReturned {

    private Long companyId;

    public ForceCompanyExportEventMessageReturned() {
    }

    public ForceCompanyExportEventMessageReturned(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
