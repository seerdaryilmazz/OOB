package ekol.orders.order.domain;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Appointment {

    @Embedded
    private FixedZoneDateTime startDateTime;
    @Embedded
    private FixedZoneDateTime endDateTime;

    public FixedZoneDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(FixedZoneDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public FixedZoneDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(FixedZoneDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
