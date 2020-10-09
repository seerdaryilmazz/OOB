package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.City;
import ekol.location.domain.Country;

/**
 * Created by ozer on 02/02/2017.
 */
public final class CityBuilder {
    private Long id;
    private Country country;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private CityBuilder() {
    }

    public static CityBuilder aCity() {
        return new CityBuilder();
    }

    public CityBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CityBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public CityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CityBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CityBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public CityBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public CityBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public City build() {
        City city = new City();
        city.setId(id);
        city.setCountry(country);
        city.setName(name);
        city.setDeleted(deleted);
        city.setDeletedAt(deletedAt);
        city.setLastUpdated(lastUpdated);
        city.setLastUpdatedBy(lastUpdatedBy);
        return city;
    }
}
