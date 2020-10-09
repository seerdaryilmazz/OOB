package ekol.mongodb.domain.datetime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.json.annotation.CustomSchema;
import ekol.json.annotation.CustomSchemaType;
import ekol.mongodb.serializer.UtcDateTimeDeserializer;
import ekol.mongodb.serializer.UtcDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by kilimci on 11/09/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = UtcDateTimeSerializer.class)
@JsonDeserialize(using = UtcDateTimeDeserializer.class)
@CustomSchema(CustomSchemaType.UTC_DATE_TIME)
public class UtcDateTime {

    //mongo db java driver takes dates as system default and writes to mongo as UTC
    //so converting dates to UTC and writing them to mongodb is wrong, because dates get converted to UTC again.
    //as a result this UTCDateTime class is not useful as it should be, we could use LocalDateTime instead,
    //we are going to keep it for a while.

    private LocalDateTime dateTime;
    public UtcDateTime() {
        // Default Constructor
    }
    public static UtcDateTime withZonedTime(ZonedDateTime zonedDateTime){
        return withLocalTime(zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());

    }
    public static UtcDateTime withLocalTime(LocalDateTime dateTime){
        UtcDateTime utcDateTime = new UtcDateTime();
        utcDateTime.setDateTime(dateTime);
        return utcDateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    private void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
