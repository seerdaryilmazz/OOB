package ekol.mongodb.domain.datetime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.json.annotation.CustomSchema;
import ekol.json.annotation.CustomSchemaType;
import ekol.mongodb.serializer.FixedZoneDateTimeWindowDeserializer;
import ekol.mongodb.serializer.FixedZoneDateTimeWindowSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = FixedZoneDateTimeWindowSerializer.class)
@JsonDeserialize(using = FixedZoneDateTimeWindowDeserializer.class)
@CustomSchema(CustomSchemaType.FIXED_ZONE_DATE_TIME_WINDOW)
public class FixedZoneDateTimeWindow implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime start;

    private LocalDateTime end;

    private String timeZone;

    public FixedZoneDateTimeWindow() {
        // Default Constructor
    }

    public FixedZoneDateTimeWindow(LocalDateTime start, LocalDateTime end, String timeZone) {
        this.start = start;
        this.end = end;
        this.timeZone = timeZone;
    }

    public FixedZoneDateTimeWindow(FixedZoneDateTime start, FixedZoneDateTime end) {

        if (start != null && end != null) {
            if (!start.getTimeZone().equals(end.getTimeZone())) {
                throw new IllegalArgumentException("Start and end time zone must be the same.");
            }
        }

        this.start = (start != null ? start.getDateTime() : null);
        this.end = (end != null ? end.getDateTime() : null);
        this.timeZone = (start != null ? start.getTimeZone() : (end != null ? end.getTimeZone() : null));
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean contains(FixedZoneDateTime dateTime) {

        boolean startIsOk;
        boolean endIsOk;

        if (getStart() == null) {
            startIsOk = true;
        } else {
            ZonedDateTime zonedDateTime1 = ZonedDateTime.of(start, ZoneId.of(timeZone));
            ZonedDateTime zonedDateTime2 = ZonedDateTime.of(dateTime.getDateTime(), ZoneId.of(dateTime.getTimeZone()));
            startIsOk = zonedDateTime1.isBefore(zonedDateTime2);
        }

        if (getEnd() == null) {
            endIsOk = true;
        } else {
            ZonedDateTime zonedDateTime1 = ZonedDateTime.of(end, ZoneId.of(timeZone));
            ZonedDateTime zonedDateTime2 = ZonedDateTime.of(dateTime.getDateTime(), ZoneId.of(dateTime.getTimeZone()));
            endIsOk = zonedDateTime1.isAfter(zonedDateTime2);
        }

        return startIsOk && endIsOk;
    }
}
