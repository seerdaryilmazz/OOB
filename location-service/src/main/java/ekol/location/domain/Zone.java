package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ozer on 13/12/16.
 */
@Entity
@Table(name = "zone")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Zone.allDetails",
                attributeNodes = {
                        @NamedAttributeNode("zoneType"),
                        @NamedAttributeNode("tags"),
                        @NamedAttributeNode(value = "zipCodes", subgraph = "ZoneZipCode.allDetails"),
                        @NamedAttributeNode(value = "polygonRegions", subgraph = "ZonePolygonRegion.allDetails")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "ZoneZipCode.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode("country"),
                                }
                        ),
                        @NamedSubgraph(
                                name = "ZonePolygonRegion.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode("zone"),
                                        @NamedAttributeNode(value = "polygonRegion", subgraph = "PolygonRegion.allDetails")
                                }
                        ),
                        @NamedSubgraph(
                                name = "PolygonRegion.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygons", subgraph = "Polygon.allDetails")
                                }
                        ),
                        @NamedSubgraph(
                                name = "Polygon.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "rings", subgraph = "CoordinateRing.allDetails")
                                }
                        ),
                        @NamedSubgraph(
                                name = "CoordinateRing.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "encodedCoordinatesString")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "Zone.withType",
                attributeNodes = {
                        @NamedAttributeNode("zoneType")
                }
        ),
        @NamedEntityGraph(
                name = "Zone.coordinateSearch",
                attributeNodes = {
                        @NamedAttributeNode(value = "polygonRegions", subgraph = "ZonePolygonRegion.coordinateSearch")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "ZonePolygonRegion.coordinateSearch",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygonRegion", subgraph = "PolygonRegion.coordinateSearch")
                                }
                        ),
                        @NamedSubgraph(
                                name = "PolygonRegion.coordinateSearch",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "polygons", subgraph = "Polygon.coordinateSearch")
                                }
                        ),
                        @NamedSubgraph(
                                name = "Polygon.coordinateSearch",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "rings")
                                }
                        )
                }
        )
})
public class Zone extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_zone", sequenceName = "seq_zone")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_zone")
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zoneTypeId")
    private ZoneType zoneType;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    @Where(clause = "deleted=0")
    private Set<ZoneTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    @Where(clause = "deleted=0")
    private Set<ZoneZipCode> zipCodes = new HashSet<>();

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    @Where(clause = "deleted=0")
    private Set<ZonePolygonRegion> polygonRegions = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZoneType getZoneType() {
        return zoneType;
    }

    public void setZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public Set<ZoneTag> getTags() {
        return tags;
    }

    public void setTags(Set<ZoneTag> tags) {
        this.tags = tags;
    }

    public Set<ZoneZipCode> getZipCodes() {
        return zipCodes;
    }

    public void setZipCodes(Set<ZoneZipCode> zipCodes) {
        this.zipCodes = zipCodes;
    }

    public Set<ZonePolygonRegion> getPolygonRegions() {
        return polygonRegions;
    }

    public void setPolygonRegions(Set<ZonePolygonRegion> polygonRegions) {
        this.polygonRegions = polygonRegions;
    }
}
