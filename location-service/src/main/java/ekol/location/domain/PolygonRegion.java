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
                name = "PolygonRegion.drawing",
                attributeNodes = {
                        @NamedAttributeNode(value = "polygons", subgraph = "Polygon.drawing")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "Polygon.drawing",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "rings", subgraph = "CoordinateRing.drawing")
                                }
                        ),
                        @NamedSubgraph(
                                name = "CoordinateRing.drawing",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "encodedCoordinatesString")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "PolygonRegion.coordinateSearch",
                attributeNodes = {
                        @NamedAttributeNode(value = "polygons", subgraph = "Polygon.coordinateSearch")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "Polygon.coordinateSearch",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "rings")
                                }
                        )
                }
        )
})
@Entity
@Table(name = "PolygonRegion")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class PolygonRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_polygon_region", sequenceName = "seq_polygon_region")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_polygon_region")
    private Long id;

    private String parent;

    private String countryIsoAlpha3Code;

    private String name;

    private String localName;

    @Column(name = "lvl")
    private Integer level;

    @OneToMany(mappedBy = "region")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<Polygon> polygons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCountryIsoAlpha3Code() {
        return countryIsoAlpha3Code;
    }

    public void setCountryIsoAlpha3Code(String countryIsoAlpha3Code) {
        this.countryIsoAlpha3Code = countryIsoAlpha3Code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public boolean getHasParent() {
        if (level == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getHasChildren() {
        if (level < 2) {
            return true;
        } else {
            return false;
        }
    }

    public Set<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(Set<Polygon> polygons) {
        this.polygons = polygons;
    }
}
