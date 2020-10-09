package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "CollRgToPolygonRg")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class CollectionRegionToPolygonRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_coll_rg_to_polygon_rg", sequenceName = "seq_coll_rg_to_polygon_rg")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_coll_rg_to_polygon_rg")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collectionRegionId")
    @JsonBackReference
    private CollectionRegion collectionRegion;

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

    public CollectionRegion getCollectionRegion() {
        return collectionRegion;
    }

    public void setCollectionRegion(CollectionRegion collectionRegion) {
        this.collectionRegion = collectionRegion;
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
