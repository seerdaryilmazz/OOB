package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.Country;
import ekol.location.domain.Region;

/**
 * Created by ozer on 02/02/2017.
 */
public final class RegionBuilder {
    private Long id;
    private Country country;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private RegionBuilder() {
    }

    public static RegionBuilder aRegion() {
        return new RegionBuilder();
    }

    public RegionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RegionBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public RegionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RegionBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public RegionBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public RegionBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public RegionBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public Region build() {
        Region region = new Region();
        region.setId(id);
        region.setCountry(country);
        region.setName(name);
        region.setDeleted(deleted);
        region.setDeletedAt(deletedAt);
        region.setLastUpdated(lastUpdated);
        region.setLastUpdatedBy(lastUpdatedBy);
        return region;
    }
}
