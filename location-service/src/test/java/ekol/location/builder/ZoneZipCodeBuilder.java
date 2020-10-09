package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.Country;
import ekol.location.domain.Zone;
import ekol.location.domain.ZoneZipCode;
import ekol.location.domain.ZoneZipCodeType;

/**
 * Created by ozer on 02/02/2017.
 */
public final class ZoneZipCodeBuilder {
    private Long id;
    private Zone zone;
    private Country country;
    private ZoneZipCodeType zoneZipCodeType;
    private String value1;
    private String value2;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private ZoneZipCodeBuilder() {
    }

    public static ZoneZipCodeBuilder aZoneZipCode() {
        return new ZoneZipCodeBuilder();
    }

    public ZoneZipCodeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ZoneZipCodeBuilder withZone(Zone zone) {
        this.zone = zone;
        return this;
    }

    public ZoneZipCodeBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public ZoneZipCodeBuilder withZoneZipCodeType(ZoneZipCodeType zoneZipCodeType) {
        this.zoneZipCodeType = zoneZipCodeType;
        return this;
    }

    public ZoneZipCodeBuilder withValue1(String value1) {
        this.value1 = value1;
        return this;
    }

    public ZoneZipCodeBuilder withValue2(String value2) {
        this.value2 = value2;
        return this;
    }

    public ZoneZipCodeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public ZoneZipCodeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ZoneZipCodeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ZoneZipCodeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public ZoneZipCode build() {
        ZoneZipCode zoneZipCode = new ZoneZipCode();
        zoneZipCode.setId(id);
        zoneZipCode.setZone(zone);
        zoneZipCode.setCountry(country);
        zoneZipCode.setZoneZipCodeType(zoneZipCodeType);
        zoneZipCode.setValue1(value1);
        zoneZipCode.setValue2(value2);
        zoneZipCode.setDeleted(deleted);
        zoneZipCode.setDeletedAt(deletedAt);
        zoneZipCode.setLastUpdated(lastUpdated);
        zoneZipCode.setLastUpdatedBy(lastUpdatedBy);
        return zoneZipCode;
    }
}
