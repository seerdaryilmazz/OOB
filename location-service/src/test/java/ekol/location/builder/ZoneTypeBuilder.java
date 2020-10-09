package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.ZoneType;

public final class ZoneTypeBuilder {

    private Long id;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private ZoneTypeBuilder() {
    }

    public static ZoneTypeBuilder aZoneType() {
        return new ZoneTypeBuilder();
    }

    public ZoneTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ZoneTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public ZoneTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ZoneTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public ZoneTypeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ZoneTypeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ZoneTypeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public ZoneType build() {
        ZoneType zoneType = new ZoneType();
        zoneType.setId(id);
        zoneType.setCode(code);
        zoneType.setName(name);
        zoneType.setDeleted(deleted);
        zoneType.setDeletedAt(deletedAt);
        zoneType.setLastUpdated(lastUpdated);
        zoneType.setLastUpdatedBy(lastUpdatedBy);
        return zoneType;
    }
}
