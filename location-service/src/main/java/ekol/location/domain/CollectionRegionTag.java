package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "CollRgTag")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class CollectionRegionTag extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_coll_rg_tag", sequenceName = "seq_coll_rg_tag")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_coll_rg_tag")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collectionRegionId")
    @JsonBackReference
    private CollectionRegion collectionRegion;

    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
