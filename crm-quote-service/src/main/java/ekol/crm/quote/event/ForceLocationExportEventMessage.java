package ekol.crm.quote.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForceLocationExportEventMessage {

    private Long locationId;

    public ForceLocationExportEventMessage() {
    }

    public ForceLocationExportEventMessage(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
