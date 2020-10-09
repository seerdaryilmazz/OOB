package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "CollectionRegion.query.one",
                attributeNodes = {
                        @NamedAttributeNode(value = "collectionRegionToPolygonRegions", subgraph = "CollectionRegionToPolygonRegion"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "CollectionRegionToPolygonRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "CollectionRegion.query.two",
                attributeNodes = {
                }
        )
})
@Entity
@Table(name = "CollRg")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class CollectionRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_coll_rg", sequenceName = "seq_coll_rg")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_coll_rg")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operationRegionId")
    @JsonBackReference
    private OperationRegion operationRegion;

    private String name;

    @OneToMany(mappedBy = "collectionRegion")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<CollectionRegionTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "collectionRegion")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<CollectionRegionToPolygonRegion> collectionRegionToPolygonRegions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationRegion getOperationRegion() {
        return operationRegion;
    }

    public void setOperationRegion(OperationRegion operationRegion) {
        this.operationRegion = operationRegion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CollectionRegionTag> getTags() {
        return tags;
    }

    public void setTags(Set<CollectionRegionTag> tags) {
        this.tags = tags;
    }

    public Set<CollectionRegionToPolygonRegion> getCollectionRegionToPolygonRegions() {
        return collectionRegionToPolygonRegions;
    }

    public void setCollectionRegionToPolygonRegions(Set<CollectionRegionToPolygonRegion> collectionRegionToPolygonRegions) {
        this.collectionRegionToPolygonRegions = collectionRegionToPolygonRegions;
    }
}
