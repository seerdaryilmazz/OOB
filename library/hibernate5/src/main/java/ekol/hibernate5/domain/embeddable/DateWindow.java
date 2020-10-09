package ekol.hibernate5.domain.embeddable;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.json.serializers.LocalDateDeserializer;
import ekol.json.serializers.LocalDateSerializer;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by fatmaozyildirim on 3/14/16.
 */
@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateWindow implements Serializable {
    private static final long serialVersionUID = 1805122041950251207L;

    @Column(nullable = true)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;

    @Column(nullable = true)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;

    public DateWindow() {
        //Default Constructor
    }

    public DateWindow(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean contains(LocalDate date) {
        return contains(date, false, false);
    }

    public boolean contains(LocalDate date, boolean startInclusive, boolean endInclusive) {
        boolean startDateIsBefore = this.getStartDate() == null || this.getStartDate().isBefore(date) || (startInclusive && this.getStartDate().isEqual(date));
        boolean endDateIsAfter = this.getEndDate() == null || this.getEndDate().isAfter(date) || (endInclusive && this.getEndDate().isEqual(date));
        return startDateIsBefore && endDateIsAfter;
    }
}
