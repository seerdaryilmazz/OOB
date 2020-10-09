package ekol.location.domain.location.port;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.location.domain.LinehaulRouteLegStop;
import ekol.location.domain.LinehaulRouteLegStoppable;
import ekol.location.domain.LocationType;
import ekol.location.domain.location.comnon.Place;
import ekol.location.domain.location.enumeration.PortRegistrationMethod;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by burak on 11/04/17.
 */
@Entity
@Table(name="port")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Port extends Place implements LinehaulRouteLegStoppable{

    @Id
    @SequenceGenerator(name = "seq_port", sequenceName = "seq_port")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_port")
    private Long id;

    @Enumerated
    private PortRegistrationMethod registrationMethod;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "routeLegStopId")
    private LinehaulRouteLegStop routeLegStop;

    private Long registrationCompanyId;
    private String registrationCompanyName;

    private Long registrationLocationId;
    private String registrationLocationName;

    private String entranceGate;

    private Integer entranceTimeFrom;
    private Integer entranceTimeTo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "port")
    @JsonManagedReference
    private Set<PortAsset> assets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PortRegistrationMethod getRegistrationMethod() {
        return registrationMethod;
    }

    public void setRegistrationMethod(PortRegistrationMethod registrationMethod) {
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

    public Set<PortAsset> getAssets() {
        return assets;
    }

    public void setAssets(Set<PortAsset> assets) {
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
        return LocationType.PORT;
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
