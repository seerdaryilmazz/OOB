package ekol.hibernate5.domain.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.json.serializers.LocalDateTimeDeserializer;
import ekol.json.serializers.LocalDateTimeSerializer;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateTimeWindow implements Serializable {

    @Column(nullable = true)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime start;

    @Column(nullable = true)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime end;

    public DateTimeWindow() {
        // Default Constructor
    }

    public DateTimeWindow(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
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

    public boolean contains(LocalDateTime dateTime) {
        boolean startIsBefore = (this.getStart() == null || this.getStart().isBefore(dateTime));
        boolean endIsAfter = (this.getEnd() == null || this.getEnd().isAfter(dateTime));
        return startIsBefore && endIsAfter;
    }
}
