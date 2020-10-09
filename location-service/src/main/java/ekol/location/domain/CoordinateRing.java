package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CoordinateRing")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class CoordinateRing extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_coordinate_ring", sequenceName = "seq_coordinate_ring")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_coordinate_ring")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "polygonId")
    @JsonBackReference
    private Polygon polygon;

    @Enumerated(EnumType.STRING)
    private CoordinateRingType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encodedCoordsStrId")
    private EncodedCoordinatesString encodedCoordinatesString;

    private BigDecimal minLongitude;

    private BigDecimal maxLongitude;

    private BigDecimal minLatitude;

    private BigDecimal maxLatitude;

    @OneToMany(mappedBy = "ring")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<Coordinate> coordinates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public CoordinateRingType getType() {
        return type;
    }

    public void setType(CoordinateRingType type) {
        this.type = type;
    }

    public EncodedCoordinatesString getEncodedCoordinatesString() {
        return encodedCoordinatesString;
    }

    public void setEncodedCoordinatesString(EncodedCoordinatesString encodedCoordinatesString) {
        this.encodedCoordinatesString = encodedCoordinatesString;
    }

    public BigDecimal getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(BigDecimal minLongitude) {
        this.minLongitude = minLongitude;
    }

    public BigDecimal getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(BigDecimal maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public BigDecimal getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(BigDecimal minLatitude) {
        this.minLatitude = minLatitude;
    }

    public BigDecimal getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(BigDecimal maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public Set<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Set<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
