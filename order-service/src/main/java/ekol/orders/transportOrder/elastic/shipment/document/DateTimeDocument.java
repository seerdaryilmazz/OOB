package ekol.orders.transportOrder.elastic.shipment.document;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeDocument {

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    private String localDateTime;

    private String timezone;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    private String utcDateTime;

    public static DateTimeDocument withFixedZoneDateTime(FixedZoneDateTime dateTime){
        DateTimeDocument document = new DateTimeDocument();
        if(dateTime == null || dateTime.getDateTime() == null){
            return null;
        }
        document.setLocalDateTime(dateTime.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        document.setTimezone(dateTime.getTimeZone());
        document.setUtcDateTime(
                LocalDateTime.ofInstant(dateTime.toZonedDateTime().toInstant(), ZoneId.of("UTC"))
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        return document;
    }


    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getUtcDateTime() {
        return utcDateTime;
    }

    public void setUtcDateTime(String utcDateTime) {
        this.utcDateTime = utcDateTime;
    }
}