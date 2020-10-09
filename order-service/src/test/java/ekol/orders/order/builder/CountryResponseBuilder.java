package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.response.location.CountryResponse;

public final class CountryResponseBuilder {
    private Long id;
    private String name;
    private String iso;
    private boolean euMember;

    private CountryResponseBuilder() {
    }

    public static CountryResponseBuilder aCountryResponse() {
        return new CountryResponseBuilder();
    }

    public CountryResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CountryResponseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CountryResponseBuilder withIso(String iso) {
        this.iso = iso;
        return this;
    }

    public CountryResponseBuilder withEuMember(boolean euMember) {
        this.euMember = euMember;
        return this;
    }

    public CountryResponseBuilder but() {
        return aCountryResponse().withId(id).withName(name).withIso(iso).withEuMember(euMember);
    }

    public CountryResponse build() {
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setId(id);
        countryResponse.setName(name);
        countryResponse.setIso(iso);
        countryResponse.setEuMember(euMember);
        return countryResponse;
    }
}
