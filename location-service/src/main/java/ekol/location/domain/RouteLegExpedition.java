package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by burak on 11/12/17.
 */
@Entity
@Table(name = "RouteLegExpedition")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteLegExpedition extends BaseEntity
{

    private static String CODE_PREFIX_SEAWAY = "1";
    private static String CODE_PREFIX_RAILWAY = "4";
    private static String CODE_PREFIX_ROAD = "7";

    @Id
    @SequenceGenerator(name = "seq_route_leg_expedition", sequenceName = "seq_route_leg_expedition")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_route_leg_expedition")
    private Long id;
    private String code;

    private Integer key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeLegId")
    private LinehaulRouteLeg linehaulRouteLeg;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "scheduleId")
    private LinehaulRouteLegSchedule parentSchedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "departureDate")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "departureDateTz"))
    })
    private FixedZoneDateTime departure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "arrivalDate")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "arrivalDateTz"))
    })
    private FixedZoneDateTime arrival;

    @Enumerated(EnumType.STRING)
    private RouteLegExpeditionStatus status;

    private int capacityUsage = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public LinehaulRouteLeg getLinehaulRouteLeg() {
        return linehaulRouteLeg;
    }

    public void setLinehaulRouteLeg(LinehaulRouteLeg linehaulRouteLeg) {
        this.linehaulRouteLeg = linehaulRouteLeg;
    }

    public LinehaulRouteLegSchedule getParentSchedule() {
        return parentSchedule;
    }

    public void setParentSchedule(LinehaulRouteLegSchedule parentSchedule) {
        this.parentSchedule = parentSchedule;
    }

    public FixedZoneDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(FixedZoneDateTime departure) {
        this.departure = departure;
    }

    public FixedZoneDateTime getArrival() {
        return arrival;
    }

    public void setArrival(FixedZoneDateTime arrival) {
        this.arrival = arrival;
    }

    public RouteLegExpeditionStatus getStatus() {
        return status;
    }

    public void setStatus(RouteLegExpeditionStatus status) {
        this.status = status;
    }

    public int getCapacityUsage() {
        return capacityUsage;
    }

    public void setCapacityUsage(int capacityUsage) {
        this.capacityUsage = capacityUsage;
    }

    public static String getCodePrefix(RouteLegType routeLegType) {
        switch (routeLegType){
            case SEAWAY:
                return CODE_PREFIX_SEAWAY;
            case RAILWAY:
                return CODE_PREFIX_RAILWAY;
            case ROAD:
                return CODE_PREFIX_ROAD;
        }
        return null;
    }
}
