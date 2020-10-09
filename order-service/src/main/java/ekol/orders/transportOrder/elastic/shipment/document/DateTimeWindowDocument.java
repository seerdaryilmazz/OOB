package ekol.orders.transportOrder.elastic.shipment.document;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

public class DateTimeWindowDocument implements Serializable {

    @Field(type = FieldType.Nested)
    private DateTimeDocument start;

    @Field(type = FieldType.Nested)
    private DateTimeDocument end;

    public DateTimeDocument getStart() {
        return start;
    }

    public void setStart(DateTimeDocument start) {
        this.start = start;
    }

    public DateTimeDocument getEnd() {
        return end;
    }

    public void setEnd(DateTimeDocument end) {
        this.end = end;
    }

    public static DateTimeWindowDocument fromFixedZoneDateTimeWindow(FixedZoneDateTimeWindow dateTimeWindow) {
        DateTimeWindowDocument document = new DateTimeWindowDocument();
        if (dateTimeWindow != null) {
            if (dateTimeWindow.getStart() != null) {
                document.setStart(
                        DateTimeDocument.withFixedZoneDateTime(
                                new FixedZoneDateTime(dateTimeWindow.getStart(), dateTimeWindow.getTimeZone())
                        )
                );
            }
            if (dateTimeWindow.getEnd() != null) {
                document.setEnd(
                        DateTimeDocument.withFixedZoneDateTime(
                                new FixedZoneDateTime(dateTimeWindow.getEnd(), dateTimeWindow.getTimeZone())
                        )
                );
            }
        }
        return document;
    }
}
