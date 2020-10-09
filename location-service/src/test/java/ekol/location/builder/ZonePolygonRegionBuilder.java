package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.PolygonRegion;
import ekol.location.domain.Zone;
import ekol.location.domain.ZonePolygonRegion;
import ekol.location.domain.ZoneZipCode;

/**
 * Created by ozer on 02/02/2017.
 */
public final class ZonePolygonRegionBuilder {
    private Long id;
    private boolean selected;
    private Zone zone;
    private PolygonRegion polygonRegion;
    private ZoneZipCode zoneZipCode;
    private String zoneZipCodeRep;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private ZonePolygonRegionBuilder() {
    }

    public static ZonePolygonRegionBuilder aZonePolygonRegion() {
        return new ZonePolygonRegionBuilder();
    }

    public ZonePolygonRegionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ZonePolygonRegionBuilder withSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public ZonePolygonRegionBuilder withZone(Zone zone) {
        this.zone = zone;
        return this;
    }

    public ZonePolygonRegionBuilder withPolygonRegion(PolygonRegion polygonRegion) {
        this.polygonRegion = polygonRegion;
        return this;
    }

    public ZonePolygonRegionBuilder withZoneZipCode(ZoneZipCode zoneZipCode) {
        this.zoneZipCode = zoneZipCode;
        return this;
    }

    public ZonePolygonRegionBuilder withZoneZipCodeRep(String zoneZipCodeRep) {
        this.zoneZipCodeRep = zoneZipCodeRep;
        return this;
    }

    public ZonePolygonRegionBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public ZonePolygonRegionBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ZonePolygonRegionBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ZonePolygonRegionBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public ZonePolygonRegion build() {
        ZonePolygonRegion zonePolygonRegion = new ZonePolygonRegion();
        zonePolygonRegion.setId(id);
        zonePolygonRegion.setSelected(selected);
        zonePolygonRegion.setZone(zone);
        zonePolygonRegion.setPolygonRegion(polygonRegion);
        zonePolygonRegion.setZoneZipCode(zoneZipCode);
        zonePolygonRegion.setZoneZipCodeRep(zoneZipCodeRep);
        zonePolygonRegion.setDeleted(deleted);
        zonePolygonRegion.setDeletedAt(deletedAt);
        zonePolygonRegion.setLastUpdated(lastUpdated);
        zonePolygonRegion.setLastUpdatedBy(lastUpdatedBy);
        return zonePolygonRegion;
    }
}
