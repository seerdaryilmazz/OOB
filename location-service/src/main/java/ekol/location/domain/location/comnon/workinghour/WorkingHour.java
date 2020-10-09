package ekol.location.domain.location.comnon.workinghour;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.location.comnon.Establishment;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Set;

/**
 * Created by burak on 03/04/17.
 */
@Entity
@Table(name = "pl_workinghour")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkingHour implements Serializable{

    @Id
    @SequenceGenerator(name = "seq_pl_workinghour", sequenceName = "seq_pl_workinghour")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pl_workinghour")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "establishmentId")
    @JsonBackReference
    private Establishment establishment;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="pl_workinghour_time",
            joinColumns=@JoinColumn(name = "workinghour_id"))
    @Embedded
    private Set<TimeWindow> timeWindows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Set<TimeWindow> getTimeWindows() {
        return timeWindows;
    }

    public void setTimeWindows(Set<TimeWindow> timeWindows) {
        this.timeWindows = timeWindows;
    }
}
