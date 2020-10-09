package ekol.crm.account.builder;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.enumaration.*;
import ekol.crm.account.domain.model.*;
import ekol.crm.account.domain.model.potential.RoadPotential;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public final class PotentialBuilder {
    private boolean deleted;
    private Long id;
    private Account account;
    private Country fromCountry;
    private Set<CountryPoint> fromCountryPoint;
    private Country toCountry;
    private Set<CountryPoint> toCountryPoint;
    private Set<ShipmentLoadingType> shipmentLoadingTypes;
    private FrequencyType frequencyType;
    private Integer frequency;
    private String serviceArea;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private LoadWeightType loadWeightType;



    public static PotentialBuilder aPotential() {
        return new PotentialBuilder();
    }

    public PotentialBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public PotentialBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PotentialBuilder withAccount(Account account) {
        this.account = account;
        return this;
    }

    public PotentialBuilder withFromCountry(Country fromCountry) {
        this.fromCountry = fromCountry;
        return this;
    }

    public PotentialBuilder withFromCountryPoint(Set<CountryPoint> fromCountryPoint) {
        this.fromCountryPoint = fromCountryPoint;
        return this;
    }

    public PotentialBuilder withToCountry(Country toCountry) {
        this.toCountry = toCountry;
        return this;
    }

    public PotentialBuilder withToCountryPoint(Set<CountryPoint> toCountryPoint) {
        this.toCountryPoint = toCountryPoint;
        return this;
    }

    public PotentialBuilder withShipmentLoadingTypes(Set<ShipmentLoadingType> shipmentLoadingTypes) {
        this.shipmentLoadingTypes = shipmentLoadingTypes;
        return this;
    }

    public PotentialBuilder withFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
        return this;
    }

    public PotentialBuilder withFrequency(Integer frequency) {
        this.frequency = frequency;
        return this;
    }

    public PotentialBuilder withServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
        return this;
    }

    public PotentialBuilder withValidityStartDate(LocalDate validityStartDate) {
        this.validityStartDate = validityStartDate;
        return this;
    }

    public PotentialBuilder withValidityEndDate(LocalDate validityEndDate) {
        this.validityEndDate = validityEndDate;
        return this;
    }

    public PotentialBuilder withLoadWeightType(LoadWeightType loadWeightType) {
        this.loadWeightType = loadWeightType;
        return this;
    }

    public PotentialBuilder but() {
        return aPotential()
                .withDeleted(deleted)
                .withId(id)
                .withAccount(account)
                .withFromCountry(fromCountry)
                .withFromCountryPoint(fromCountryPoint)
                .withToCountry(toCountry)
                .withToCountryPoint(toCountryPoint)
                .withShipmentLoadingTypes(shipmentLoadingTypes)
                .withFrequencyType(frequencyType)
                .withFrequency(frequency)
                .withServiceArea(serviceArea)
                .withValidityStartDate(validityStartDate)
                .withValidityEndDate(validityEndDate)
                .withLoadWeightType(loadWeightType);
    }

    public RoadPotential build() {
        RoadPotential potential = new RoadPotential();
        potential.setDeleted(deleted);
        potential.setId(id);
        potential.setAccount(account);
        potential.setFromCountry(fromCountry);
        potential.setFromCountryPoint(fromCountryPoint);
        potential.setToCountry(toCountry);
        potential.setToCountryPoint(toCountryPoint);
        potential.setShipmentLoadingTypes(shipmentLoadingTypes);
        potential.setLoadWeightType(loadWeightType);
        potential.setFrequencyType(frequencyType);
        potential.setFrequency(frequency);
        potential.setValidityStartDate(validityStartDate);
        potential.setValidityEndDate(validityEndDate);
        return potential;
    }
}
