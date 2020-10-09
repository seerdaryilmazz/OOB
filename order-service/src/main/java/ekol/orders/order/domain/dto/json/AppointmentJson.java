package ekol.orders.order.domain.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.order.domain.Appointment;
import ekol.orders.order.domain.serializer.ZonedDateTimeDeserializer;
import ekol.orders.order.domain.serializer.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentJson {

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime startDateTime;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime endDateTime;

    public static AppointmentJson fromEntity(Appointment appointment){
        AppointmentJson json = new AppointmentJson();
        if(appointment.getStartDateTime() != null){
            json.setStartDateTime(appointment.getStartDateTime().toZonedDateTime());
        }
        if(appointment.getEndDateTime() != null){
            json.setEndDateTime(appointment.getEndDateTime().toZonedDateTime());
        }
        return json;
    }

    public Appointment toEntity(){
        Appointment appointment = new Appointment();
        if(getStartDateTime() != null){
            appointment.setStartDateTime(new FixedZoneDateTime(getStartDateTime().toLocalDateTime(), getStartDateTime().getZone().getId()));
        }
        if(getEndDateTime() != null){
            appointment.setEndDateTime(new FixedZoneDateTime(getEndDateTime().toLocalDateTime(), getEndDateTime().getZone().getId()));
        }
        return appointment;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
