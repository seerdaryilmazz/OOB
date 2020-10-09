package ekol.mongodb.domain.datetime;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.json.annotation.CustomSchema;
import ekol.json.annotation.CustomSchemaType;
import ekol.mongodb.serializer.FixedZoneDateTimeDeserializer;
import ekol.mongodb.serializer.FixedZoneDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = FixedZoneDateTimeSerializer.class)
@JsonDeserialize(using = FixedZoneDateTimeDeserializer.class)
@CustomSchema(CustomSchemaType.FIXED_ZONE_DATE_TIME)
public class FixedZoneDateTime implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime dateTime;
    private String timeZone;

    public static FixedZoneDateTime parse(String dateTimeStr){
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV"));
        return FixedZoneDateTime.withZonedTime(zonedDateTime);
    }

    public static FixedZoneDateTime withLocalTimeAndTimezone(LocalDateTime dateTime, String timeZone){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of(timeZone));
        return FixedZoneDateTime.withZonedTime(zonedDateTime);
    }

    public static FixedZoneDateTime withZonedTime(ZonedDateTime dateTime){
        FixedZoneDateTime fixedZoneDateTime = new FixedZoneDateTime();
        fixedZoneDateTime.setTimeZone(dateTime.getZone().getId());
        fixedZoneDateTime.setDateTime(dateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        return fixedZoneDateTime;
    }

    public static FixedZoneDateTime nowWithTimezone(String timezone){
        return FixedZoneDateTime.withZonedTime(ZonedDateTime.now(ZoneId.of(timezone)));
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
        return ZonedDateTime.of(dateTime, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(timeZone));
    }
}
