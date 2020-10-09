package ekol.location.domain.location.comnon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by burak on 03/04/17.
 */
@Entity
@Table(name="plLocation")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_pl_location", sequenceName = "seq_pl_location")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pl_location")
    private Long id;

    private String googlePlaceId;

    private String googlePlaceUrl;

    private String timezone;

    @Embedded
    private Point pointOnMap;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PL_LOCATION_POLYGON_PATH", joinColumns = @JoinColumn(name = "locationId"))
    @Embedded
    @OrderBy("sortIndex ASC")
    private Set<PolygonPoint> polygonPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public String getGooglePlaceUrl() {
        return googlePlaceUrl;
    }

    public void setGooglePlaceUrl(String googlePlaceUrl) {
        this.googlePlaceUrl = googlePlaceUrl;
    }

    public Point getPointOnMap() {
        return pointOnMap;
    }

    public void setPointOnMap(Point pointOnMap) {
        this.pointOnMap = pointOnMap;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Set<PolygonPoint> getPolygonPath() {
        //TODO: need tofix this via hibernate. This is a work around.
        // It needs to Return null if size is 0 but hibernatereturns empty list
        return polygonPath!= null && polygonPath.size() > 0 ? polygonPath : null;
    }

    public void setPolygonPath(Set<PolygonPoint> polygonPath) {
        this.polygonPath = polygonPath;
    }
}
