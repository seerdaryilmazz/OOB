package ekol.orders.transportOrder.elastic.shipment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.elastic.shipment.document.LocationDocument;

/**
 * Created by ozer on 06/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FromOrTo {

    private LocationType locationType;
    private Long locationId;
    private LocationDocument locationDocument;

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public LocationDocument getLocationDocument() {
        return locationDocument;
    }

    public void setLocationDocument(LocationDocument locationDocument) {
        this.locationDocument = locationDocument;
    }
}
