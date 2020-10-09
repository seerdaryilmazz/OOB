package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "DistRgToPolygonRg")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class DistributionRegionToPolygonRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_dist_rg_to_polygon_rg", sequenceName = "seq_dist_rg_to_polygon_rg")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dist_rg_to_polygon_rg")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distributionRegionId")
    @JsonBackReference
    private DistributionRegion distributionRegion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "polygonRegionId")
    private PolygonRegion polygonRegion;

    @Enumerated(EnumType.STRING)
    private RegionCategory category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DistributionRegion getDistributionRegion() {
        return distributionRegion;
    }

    public void setDistributionRegion(DistributionRegion distributionRegion) {
        this.distributionRegion = distributionRegion;
    }

    public PolygonRegion getPolygonRegion() {
        return polygonRegion;
    }

    public void setPolygonRegion(PolygonRegion polygonRegion) {
        this.polygonRegion = polygonRegion;
    }

    public RegionCategory getCategory() {
        return category;
    }

    public void setCategory(RegionCategory category) {
        this.category = category;
    }
}
