package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.Country;
import ekol.location.domain.LinehaulRouteLegStop;
import ekol.location.domain.LocationType;
import ekol.location.domain.location.comnon.Establishment;
import ekol.location.domain.location.comnon.Location;
import ekol.location.domain.location.comnon.workinghour.WorkingHour;
import ekol.location.domain.location.enumeration.PortRegistrationMethod;
import ekol.location.domain.location.port.Port;
import ekol.location.domain.location.port.PortAsset;

import java.util.Set;

/**
 * Created by kilimci on 28/04/2017.
 */
public final class PortBuilder {
    private String name;
    private String localName;
    private Boolean active;
    private Location location;
    private Establishment establishment;
    private Long id;
    private PortRegistrationMethod registrationMethod;
    private Long registrationCompanyId;
    private String registrationCompanyName;
    private Long registrationLocationId;
    private String registrationLocationName;
    private String entranceGate;
    private Integer entranceTimeFrom;
    private Integer entranceTimeTo;
    private Set<PortAsset> assets;
    private LinehaulRouteLegStop routeLegStop;
    private LocationType type;

    private Country country;
    private String countryName;
    private String countryIso;
    private Set<WorkingHour> workingHours;

    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private PortBuilder() {
    }

    public static PortBuilder aPort() {
        return new PortBuilder();
    }

    public PortBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PortBuilder withLocalName(String localName) {
        this.localName = localName;
        return this;
    }

    public PortBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public PortBuilder withLocation(Location location) {
        this.location = location;
        return this;
    }

    public PortBuilder withEstablishment(Establishment establishment) {
        this.establishment = establishment;
        return this;
    }

    public PortBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PortBuilder withRegistrationMethod(PortRegistrationMethod registrationMethod) {
        this.registrationMethod = registrationMethod;
        return this;
    }

    public PortBuilder withRegistrationCompanyId(Long registrationCompanyId) {
        this.registrationCompanyId = registrationCompanyId;
        return this;
    }

    public PortBuilder withRegistrationCompanyName(String registrationCompanyName) {
        this.registrationCompanyName = registrationCompanyName;
        return this;
    }

    public PortBuilder withRegistrationLocationId(Long registrationLocationId) {
        this.registrationLocationId = registrationLocationId;
        return this;
    }

    public PortBuilder withRegistrationLocationName(String registrationLocationName) {
        this.registrationLocationName = registrationLocationName;
        return this;
    }

    public PortBuilder withEntranceGate(String entranceGate) {
        this.entranceGate = entranceGate;
        return this;
    }

    public PortBuilder withEntranceTimeFrom(Integer entranceTimeFrom) {
        this.entranceTimeFrom = entranceTimeFrom;
        return this;
    }

    public PortBuilder withEntranceTimeTo(Integer entranceTimeTo) {
        this.entranceTimeTo = entranceTimeTo;
        return this;
    }

    public PortBuilder withAssets(Set<PortAsset> assets) {
        this.assets = assets;
        return this;
    }

    public PortBuilder withRouteLegStop(LinehaulRouteLegStop routeLegStop) {
        this.routeLegStop = routeLegStop;
        return this;
    }

    public PortBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public PortBuilder withCountryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public PortBuilder withCountryIso(String countryIso) {
        this.countryIso = countryIso;
        return this;
    }

    public PortBuilder withWorkingHours(Set<WorkingHour> workingHours){
        this.workingHours = workingHours;
        return this;
    }

    public PortBuilder withType(LocationType type){
        this.type = type;
        return this;
    }

    public PortBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public PortBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public PortBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public PortBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public PortBuilder but() {
        return aPort().withName(name).withLocalName(localName).withActive(active).withLocation(location).withEstablishment(establishment).withId(id).withRegistrationMethod(registrationMethod).withRegistrationCompanyId(registrationCompanyId).withRegistrationCompanyName(registrationCompanyName).withRegistrationLocationId(registrationLocationId).withRegistrationLocationName(registrationLocationName).withEntranceGate(entranceGate).withEntranceTimeFrom(entranceTimeFrom).withEntranceTimeTo(entranceTimeTo).withAssets(assets);
    }

    public Port build() {
        Port port = new Port();
        port.setName(name);
        port.setLocalName(localName);
        port.setActive(active);
        port.setLocation(location);
        port.setEstablishment(establishment);
        port.setId(id);
        port.setRegistrationMethod(registrationMethod);
        port.setRegistrationCompanyId(registrationCompanyId);
        port.setRegistrationCompanyName(registrationCompanyName);
        port.setRegistrationLocationId(registrationLocationId);
        port.setRegistrationLocationName(registrationLocationName);
        port.setEntranceGate(entranceGate);
        port.setEntranceTimeFrom(entranceTimeFrom);
        port.setEntranceTimeTo(entranceTimeTo);
        port.setAssets(assets);
        port.setCountry(country);
        port.setRouteLegStop(routeLegStop);

        port.setDeleted(deleted);
        port.setDeletedAt(deletedAt);
        port.setLastUpdated(lastUpdated);
        port.setLastUpdatedBy(lastUpdatedBy);

        return port;
    }
}
