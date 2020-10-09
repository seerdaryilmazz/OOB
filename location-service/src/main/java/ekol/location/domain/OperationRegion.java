package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "OperationRegion.query.one",
                attributeNodes = {
                        @NamedAttributeNode(value = "tags"),
                        @NamedAttributeNode(value = "operationRegionToPolygonRegions", subgraph = "OperationRegionToPolygonRegion"),
                        @NamedAttributeNode(value = "collectionRegions", subgraph = "CollectionRegion"),
                        @NamedAttributeNode(value = "distributionRegions", subgraph = "DistributionRegion")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "OperationRegionToPolygonRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion")
                                }
                        ),
                        @NamedSubgraph(
                                name = "CollectionRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "tags"),
                                        @NamedAttributeNode(value = "collectionRegionToPolygonRegions", subgraph = "CollectionRegionToPolygonRegion")
                                }
                        ),
                        @NamedSubgraph(
                                name = "CollectionRegionToPolygonRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion")
                                }
                        ),
                        @NamedSubgraph(
                                name = "DistributionRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "tags"),
                                        @NamedAttributeNode(value = "distributionRegionToPolygonRegions", subgraph = "DistributionRegionToPolygonRegion")
                                }
                        ),
                        @NamedSubgraph(
                                name = "DistributionRegionToPolygonRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "OperationRegion.query.two",
                attributeNodes = {
                }
        ),
        @NamedEntityGraph(
                name = "OperationRegion.query.three",
                attributeNodes = {
                        @NamedAttributeNode(value = "collectionRegions"),
                        @NamedAttributeNode(value = "distributionRegions")
                }
        ),
        @NamedEntityGraph(
                name = "OperationRegion.query.four",
                attributeNodes = {
                        @NamedAttributeNode(value = "operationRegionToPolygonRegions", subgraph = "OperationRegionToPolygonRegion"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "OperationRegionToPolygonRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "OperationRegion.query.five",
                attributeNodes = {
                        @NamedAttributeNode(value = "tags"),
                        @NamedAttributeNode(value = "collectionRegions", subgraph = "CollectionRegion"),
                        @NamedAttributeNode(value = "distributionRegions", subgraph = "DistributionRegion")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "CollectionRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "tags"),
                                        @NamedAttributeNode(value = "collectionRegionToPolygonRegions", subgraph = "CollectionRegionToPolygonRegion")
                                }
                        ),
                        @NamedSubgraph(
                                name = "CollectionRegionToPolygonRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion")
                                }
                        ),
                        @NamedSubgraph(
                                name = "DistributionRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "tags"),
                                        @NamedAttributeNode(value = "distributionRegionToPolygonRegions", subgraph = "DistributionRegionToPolygonRegion")
                                }
                        ),
                        @NamedSubgraph(
                                name = "DistributionRegionToPolygonRegion",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion")
                                }
                        )
                }
        )
})
@Entity
@Table(name = "OperationRg")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class OperationRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_operation_rg", sequenceName = "seq_operation_rg")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_operation_rg")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "operationRegion")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<OperationRegionTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "operationRegion")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<OperationRegionToPolygonRegion> operationRegionToPolygonRegions = new HashSet<>();

    @OneToMany(mappedBy = "operationRegion")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<CollectionRegion> collectionRegions = new HashSet<>();

    @OneToMany(mappedBy = "operationRegion")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<DistributionRegion> distributionRegions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<OperationRegionTag> getTags() {
        return tags;
    }

    public void setTags(Set<OperationRegionTag> tags) {
        this.tags = tags;
    }

    public Set<OperationRegionToPolygonRegion> getOperationRegionToPolygonRegions() {
        return operationRegionToPolygonRegions;
    }

    public void setOperationRegionToPolygonRegions(Set<OperationRegionToPolygonRegion> operationRegionToPolygonRegions) {
        this.operationRegionToPolygonRegions = operationRegionToPolygonRegions;
    }

    public Set<CollectionRegion> getCollectionRegions() {
        return collectionRegions;
    }

    public void setCollectionRegions(Set<CollectionRegion> collectionRegions) {
        this.collectionRegions = collectionRegions;
    }

    public Set<DistributionRegion> getDistributionRegions() {
        return distributionRegions;
    }

    public void setDistributionRegions(Set<DistributionRegion> distributionRegions) {
        this.distributionRegions = distributionRegions;
    }
}
