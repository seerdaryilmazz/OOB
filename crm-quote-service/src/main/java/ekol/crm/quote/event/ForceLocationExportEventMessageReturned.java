package ekol.crm.quote.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForceLocationExportEventMessageReturned {

    private Long locationId;

    public ForceLocationExportEventMessageReturned() {
    }

    public ForceLocationExportEventMessageReturned(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
