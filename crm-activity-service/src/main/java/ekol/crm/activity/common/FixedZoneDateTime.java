package ekol.crm.activity.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = FixedZoneDateTimeSerializer.class)
@JsonDeserialize(using = FixedZoneDateTimeDeserializer.class)
public class FixedZoneDateTime implements Serializable {

    private Instant dateTimeUtc;

    private String timeZone;

    public FixedZoneDateTime() {
    }

    public FixedZoneDateTime(Instant dateTimeUtc, String timeZone) {
        this.dateTimeUtc = dateTimeUtc;
        this.timeZone = timeZone;
    }

    public Instant getDateTimeUtc() {
        return dateTimeUtc;
    }

    public void setDateTimeUtc(Instant dateTimeUtc) {
        this.dateTimeUtc = dateTimeUtc;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public ZonedDateTime toZonedDateTime() {
        return this.dateTimeUtc.atZone(ZoneId.of(this.timeZone));
    }

    public Instant toInstant() {
        return toZonedDateTime().toInstant();
    }
}
