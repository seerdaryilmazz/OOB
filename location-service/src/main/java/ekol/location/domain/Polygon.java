package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Polygon")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class Polygon extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_polygon", sequenceName = "seq_polygon")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_polygon")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId")
    @JsonBackReference
    private PolygonRegion region;

    @OneToMany(mappedBy = "polygon")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<CoordinateRing> rings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PolygonRegion getRegion() {
        return region;
    }

    public void setRegion(PolygonRegion region) {
        this.region = region;
    }

    public Set<CoordinateRing> getRings() {
        return rings;
    }

    public void setRings(Set<CoordinateRing> rings) {
        this.rings = rings;
    }
}
