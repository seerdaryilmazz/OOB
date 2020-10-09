package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.Country;
import ekol.location.domain.location.comnon.Address;
import ekol.location.domain.location.comnon.Establishment;
import ekol.location.domain.location.comnon.IdNameEmbeddable;
import ekol.location.domain.location.comnon.PhoneNumberWithType;
import ekol.location.domain.location.comnon.workinghour.WorkingHour;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kilimci on 28/04/2017.
 */
public final class EstablishmentBuilder {
    private Long id;
    private Set<PhoneNumberWithType> phoneNumbers = new HashSet<>();
    private Set<WorkingHour> workingHours;
    private IdNameEmbeddable owner;
    private Address address;

    private Country country;
    private String countryName;
    private String countryIso;

    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private EstablishmentBuilder() {
    }

    public static EstablishmentBuilder anEstablishment() {
        return new EstablishmentBuilder();
    }

    public EstablishmentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EstablishmentBuilder withPhoneNumbers(Set<PhoneNumberWithType> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }

    public EstablishmentBuilder withWorkingHours(Set<WorkingHour> workingHours) {
        this.workingHours = workingHours;
        return this;
    }

    public EstablishmentBuilder withOwner(IdNameEmbeddable owner) {
        this.owner = owner;
        return this;
    }

    public EstablishmentBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public EstablishmentBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public EstablishmentBuilder withCountryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public EstablishmentBuilder withCountryIso(String countryIso) {
        this.countryIso = countryIso;
        return this;
    }

    public EstablishmentBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public EstablishmentBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public EstablishmentBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public EstablishmentBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public EstablishmentBuilder but() {
        return anEstablishment().withId(id).withPhoneNumbers(phoneNumbers).withWorkingHours(workingHours).withOwner(owner).withAddress(address);
    }

    public Establishment build() {
        Establishment establishment = new Establishment();
        establishment.setId(id);
        establishment.setPhoneNumbers(phoneNumbers);
        establishment.setWorkingHours(workingHours);
        establishment.setOwner(owner);
        establishment.setAddress(address);
        establishment.setCountry(country);

        establishment.setDeleted(deleted);
        establishment.setDeletedAt(deletedAt);
        establishment.setLastUpdated(lastUpdated);
        establishment.setLastUpdatedBy(lastUpdatedBy);

        return establishment;
    }
}
