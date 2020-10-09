package ekol.crm.quote.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FakeCompanyImportEventMessage {

    private List<Long> locationIds;

    public FakeCompanyImportEventMessage() {
    }

    public FakeCompanyImportEventMessage(List<Long> locationIds) {
        this.locationIds = locationIds;
    }

    public List<Long> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<Long> locationIds) {
        this.locationIds = locationIds;
    }
}
