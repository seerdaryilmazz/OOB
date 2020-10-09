package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ozer on 02/02/2017.
 */
public final class ZoneBuilder {
    private Long id;
    private String name;
    private String description;
    private ZoneType zoneType;
    private Set<ZoneTag> tags = new HashSet<>();
    private Set<ZoneZipCode> zipCodes = new HashSet<>();
    private Set<ZonePolygonRegion> polygonRegions = new HashSet<>();
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private ZoneBuilder() {
    }

    public static ZoneBuilder aZone() {
        return new ZoneBuilder();
    }

    public ZoneBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ZoneBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ZoneBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ZoneBuilder withZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
        return this;
    }

    public ZoneBuilder withTags(Set<ZoneTag> tags) {
        this.tags = tags;
        return this;
    }

    public ZoneBuilder withZipCodes(Set<ZoneZipCode> zipCodes) {
        this.zipCodes = zipCodes;
        return this;
    }

    public ZoneBuilder withPolygonRegions(Set<ZonePolygonRegion> polygonRegions) {
        this.polygonRegions = polygonRegions;
        return this;
    }

    public ZoneBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public ZoneBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ZoneBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ZoneBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public Zone build() {
        Zone zone = new Zone();
        zone.setId(id);
        zone.setName(name);
        zone.setDescription(description);
        zone.setZoneType(zoneType);
        zone.setTags(tags);
        zone.setZipCodes(zipCodes);
        zone.setPolygonRegions(polygonRegions);
        zone.setDeleted(deleted);
        zone.setDeletedAt(deletedAt);
        zone.setLastUpdated(lastUpdated);
        zone.setLastUpdatedBy(lastUpdatedBy);
        return zone;
    }
}
