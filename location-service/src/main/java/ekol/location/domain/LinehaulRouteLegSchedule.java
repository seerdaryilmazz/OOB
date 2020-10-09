package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

@Entity
@Table(name = "LhaulRouteLegSchdl")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinehaulRouteLegSchedule extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_lhaul_route_leg_schdl", sequenceName = "seq_lhaul_route_leg_schdl")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lhaul_route_leg_schdl")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private LinehaulRouteLeg parent;

    private Long companyId;

    private String companyName;

    /**
     * Deniz taşımacılığı olduğunda geminin adı bu alanda tutulacak.
     */
    private String ferryName;

    @Enumerated(EnumType.STRING)
    private DayOfWeek departureDay;

    private LocalTime departureTime;

    /**
     * Yol kaç dakika sürüyor? Başka deyişle kalkış zamanı ile varış zamanı arasındaki süre kaç dakika?
     */
    private Integer duration;

    private Integer capacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinehaulRouteLeg getParent() {
        return parent;
    }

    public void setParent(LinehaulRouteLeg parent) {
        this.parent = parent;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFerryName() {
        return ferryName;
    }

    public void setFerryName(String ferryName) {
        this.ferryName = ferryName;
    }

    public DayOfWeek getDepartureDay() {
        return departureDay;
    }

    public void setDepartureDay(DayOfWeek departureDay) {
        this.departureDay = departureDay;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public DayOfWeek getArrivalDay() {

        DayOfWeek arrivalDay = null;

        if (getDepartureDay() != null && getDepartureTime() != null && getDuration() != null) {
            int numberOfDaysToAdd = (getDepartureTime().get(ChronoField.MINUTE_OF_DAY) + getDuration()) / (24 * 60);
            arrivalDay = getDepartureDay().plus(numberOfDaysToAdd);
        }

        return arrivalDay;
    }

    public LocalTime getArrivalTime() {

        LocalTime arrivalTime = null;

        if (getDepartureTime() != null && getDuration() != null) {
            arrivalTime = getDepartureTime().plusMinutes(getDuration());
        }

        return arrivalTime;
    }
}
