package ekol.location.domain.location.terminal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.location.domain.LinehaulRouteLegStop;
import ekol.location.domain.LinehaulRouteLegStoppable;
import ekol.location.domain.LocationType;
import ekol.location.domain.location.comnon.Place;
import ekol.location.domain.location.enumeration.TerminalRegistrationMethod;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by kilimci on 28/04/2017.
 */
@Entity
@Table(name="terminal")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Terminal extends Place implements LinehaulRouteLegStoppable{
    @Id
    @SequenceGenerator(name = "seq_terminal", sequenceName = "seq_terminal")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_terminal")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "routeLegStopId")
    private LinehaulRouteLegStop routeLegStop;

    @Enumerated
    private TerminalRegistrationMethod registrationMethod;

    private Long registrationCompanyId;
    private String registrationCompanyName;

    private Long registrationLocationId;
    private String registrationLocationName;

    private String entranceGate;

    private Integer entranceTimeFrom;
    private Integer entranceTimeTo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "terminal")
    @JsonManagedReference
    private Set<TerminalAsset> assets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TerminalRegistrationMethod getRegistrationMethod() {
        return registrationMethod;
    }

    public void setRegistrationMethod(TerminalRegistrationMethod registrationMethod) {
        this.registrationMethod = registrationMethod;
    }

    public Long getRegistrationCompanyId() {
        return registrationCompanyId;
    }

    public void setRegistrationCompanyId(Long registrationCompanyId) {
        this.registrationCompanyId = registrationCompanyId;
    }

    public String getRegistrationCompanyName() {
        return registrationCompanyName;
    }

    public void setRegistrationCompanyName(String registrationCompanyName) {
        this.registrationCompanyName = registrationCompanyName;
    }

    public Long getRegistrationLocationId() {
        return registrationLocationId;
    }

    public void setRegistrationLocationId(Long registrationLocationId) {
        this.registrationLocationId = registrationLocationId;
    }

    public String getRegistrationLocationName() {
        return registrationLocationName;
    }

    public void setRegistrationLocationName(String registrationLocationName) {
        this.registrationLocationName = registrationLocationName;
    }

    public String getEntranceGate() {
        return entranceGate;
    }

    public void setEntranceGate(String entranceGate) {
        this.entranceGate = entranceGate;
    }

    public Integer getEntranceTimeFrom() {
        return entranceTimeFrom;
    }

    public void setEntranceTimeFrom(Integer entranceTimeFrom) {
        this.entranceTimeFrom = entranceTimeFrom;
    }

    public Integer getEntranceTimeTo() {
        return entranceTimeTo;
    }

    public void setEntranceTimeTo(Integer entranceTimeTo) {
        this.entranceTimeTo = entranceTimeTo;
    }

    public Set<TerminalAsset> getAssets() {
        return assets;
    }

    public void setAssets(Set<TerminalAsset> assets) {
        this.assets = assets;
    }

    @Override
    public LinehaulRouteLegStop getRouteLegStop() {
        return routeLegStop;
    }

    public void setRouteLegStop(LinehaulRouteLegStop routeLegStop) {
        this.routeLegStop = routeLegStop;
    }

    @Override
    public LocationType getType() {
        return LocationType.TRAIN_TERMINAL;
    }

    public LinehaulRouteLegStop buildRouteLegStop() {
        if(getRouteLegStop() != null){
            LinehaulRouteLegStop routeLegStop = getRouteLegStop();
            routeLegStop.copyFrom(this);
            return routeLegStop;
        }
        return LinehaulRouteLegStop.withPlace(this);
    }
}
