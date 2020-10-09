package ekol.orders.transportOrder.elastic.shipment.document;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by ozer on 30/11/16.
 */
public class TripStopDocument {

    @Field(type = FieldType.Nested)
    private LocationDocument location;

    public TripStopDocument() {
    }

    public TripStopDocument(LocationDocument location) {
        this.location = location;
    }

    public LocationDocument getLocation() {
        return location;
    }

    public void setLocation(LocationDocument location) {
        this.location = location;
    }
}
