package ekol.orders.transportOrder.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;

import java.time.LocalDateTime;

/**
 * Created by kilimci on 29/11/2017.
 */
public class ShipmentStatusChangeMessage {

    private Long shipmentId;
    private DateWithTimeZone dateTime;


    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public DateWithTimeZone getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateWithTimeZone dateTime) {
        this.dateTime = dateTime;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DateWithTimeZone {
        private LocalDateTime dateTime;
        private String timezone;

        public FixedZoneDateTime toFixedZoneDateTime(){
            FixedZoneDateTime fixedZoneDateTime = new FixedZoneDateTime();
            fixedZoneDateTime.setDateTime(dateTime);
            fixedZoneDateTime.setTimeZone(timezone);
            return fixedZoneDateTime;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }
}
