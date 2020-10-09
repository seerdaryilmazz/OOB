package ekol.hibernate5.domain.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.serializer.UtcDateTimeDeserializer;
import ekol.hibernate5.domain.serializer.UtcDateTimeSerializer;
import ekol.json.annotation.CustomSchema;
import ekol.json.annotation.CustomSchemaType;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = UtcDateTimeSerializer.class)
@JsonDeserialize(using = UtcDateTimeDeserializer.class)
@CustomSchema(CustomSchemaType.UTC_DATE_TIME)
public class UtcDateTime implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime dateTime;

    public UtcDateTime() {
        // Default Constructor
    }


    public UtcDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
