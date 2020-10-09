package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.Zone;
import ekol.location.domain.ZoneTag;

/**
 * Created by ozer on 02/02/2017.
 */
public final class ZoneTagBuilder {
    private Long id;
    private Zone zone;
    private String value;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private ZoneTagBuilder() {
    }

    public static ZoneTagBuilder aZoneTag() {
        return new ZoneTagBuilder();
    }

    public ZoneTagBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ZoneTagBuilder withZone(Zone zone) {
        this.zone = zone;
        return this;
    }

    public ZoneTagBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public ZoneTagBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public ZoneTagBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ZoneTagBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ZoneTagBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public ZoneTag build() {
        ZoneTag zoneTag = new ZoneTag();
        zoneTag.setId(id);
        zoneTag.setZone(zone);
        zoneTag.setValue(value);
        zoneTag.setDeleted(deleted);
        zoneTag.setDeletedAt(deletedAt);
        zoneTag.setLastUpdated(lastUpdated);
        zoneTag.setLastUpdatedBy(lastUpdatedBy);
        return zoneTag;
    }
}
