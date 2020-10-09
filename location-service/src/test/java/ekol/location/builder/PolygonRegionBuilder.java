package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.Polygon;
import ekol.location.domain.PolygonRegion;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ozer on 02/02/2017.
 */
public final class PolygonRegionBuilder {
    private Long id;
    private String parent;
    private String countryIsoAlpha3Code;
    private String name;
    private String localName;
    private Integer level;
    private Set<Polygon> polygons = new HashSet<>();
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private PolygonRegionBuilder() {
    }

    public static PolygonRegionBuilder aPolygonRegion() {
        return new PolygonRegionBuilder();
    }

    public PolygonRegionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PolygonRegionBuilder withParent(String parent) {
        this.parent = parent;
        return this;
    }

    public PolygonRegionBuilder withCountryIsoAlpha3Code(String countryIsoAlpha3Code) {
        this.countryIsoAlpha3Code = countryIsoAlpha3Code;
        return this;
    }

    public PolygonRegionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PolygonRegionBuilder withLocalName(String localName) {
        this.localName = localName;
        return this;
    }

    public PolygonRegionBuilder withLevel(Integer level) {
        this.level = level;
        return this;
    }

    public PolygonRegionBuilder withPolygons(Set<Polygon> polygons) {
        this.polygons = polygons;
        return this;
    }

    public PolygonRegionBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public PolygonRegionBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public PolygonRegionBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public PolygonRegionBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public PolygonRegion build() {
        PolygonRegion polygonRegion = new PolygonRegion();
        polygonRegion.setId(id);
        polygonRegion.setParent(parent);
        polygonRegion.setCountryIsoAlpha3Code(countryIsoAlpha3Code);
        polygonRegion.setName(name);
        polygonRegion.setLocalName(localName);
        polygonRegion.setLevel(level);
        polygonRegion.setPolygons(polygons);
        polygonRegion.setDeleted(deleted);
        polygonRegion.setDeletedAt(deletedAt);
        polygonRegion.setLastUpdated(lastUpdated);
        polygonRegion.setLastUpdatedBy(lastUpdatedBy);
        return polygonRegion;
    }
}
