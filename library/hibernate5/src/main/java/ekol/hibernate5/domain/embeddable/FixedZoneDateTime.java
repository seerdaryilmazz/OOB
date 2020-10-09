package ekol.hibernate5.domain.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.serializer.FixedZoneDateTimeDeserializer;
import ekol.hibernate5.domain.serializer.FixedZoneDateTimeSerializer;
import ekol.json.annotation.CustomSchema;
import ekol.json.annotation.CustomSchemaType;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = FixedZoneDateTimeSerializer.class)
@JsonDeserialize(using = FixedZoneDateTimeDeserializer.class)
@CustomSchema(CustomSchemaType.FIXED_ZONE_DATE_TIME)
public class FixedZoneDateTime implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime dateTime;

    private String timeZone;

    public FixedZoneDateTime() {
        // Default Constructor
    }

    public FixedZoneDateTime(String value) {
        int indexToSplit = value.lastIndexOf(" ");
        if(indexToSplit != -1){
            this.dateTime = LocalDateTime.parse(value.substring(0, indexToSplit), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            this.timeZone = value.substring(indexToSplit+1);
        }
    }

    public FixedZoneDateTime(LocalDateTime dateTime, String timeZone) {
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.of(dateTime, ZoneId.of(timeZone));
    }
}
