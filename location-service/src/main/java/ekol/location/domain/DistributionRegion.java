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
                name = "DistributionRegion.query.one",
                attributeNodes = {
                        @NamedAttributeNode(value = "distributionRegionToPolygonRegions", subgraph = "DistributionRegionToPolygonRegion"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "DistributionRegionToPolygonRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "DistributionRegion.query.two",
                attributeNodes = {
                }
        )
})
@Entity
@Table(name = "DistRg")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class DistributionRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_dist_rg", sequenceName = "seq_dist_rg")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dist_rg")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operationRegionId")
    @JsonBackReference
    private OperationRegion operationRegion;

    private String name;

    @OneToMany(mappedBy = "distributionRegion")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<DistributionRegionTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "distributionRegion")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<DistributionRegionToPolygonRegion> distributionRegionToPolygonRegions = new HashSet<>();

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

    public Set<DistributionRegionTag> getTags() {
        return tags;
    }

    public void setTags(Set<DistributionRegionTag> tags) {
        this.tags = tags;
    }

    public Set<DistributionRegionToPolygonRegion> getDistributionRegionToPolygonRegions() {
        return distributionRegionToPolygonRegions;
    }

    public void setDistributionRegionToPolygonRegions(Set<DistributionRegionToPolygonRegion> distributionRegionToPolygonRegions) {
        this.distributionRegionToPolygonRegions = distributionRegionToPolygonRegions;
    }
}
