package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.response.kartoteks.Country;

public class CountryBuilder {

    private Long id;
    private String countryName;
    private String iso;
    private boolean euMember;

    private CountryBuilder() {
    }

    public static CountryBuilder aCountry() {
        return new CountryBuilder();
    }

    public CountryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CountryBuilder withCountryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public CountryBuilder withIso(String iso) {
        this.iso = iso;
        return this;
    }

    public CountryBuilder withEuMember(boolean euMember) {
        this.euMember = euMember;
        return this;
    }

    public CountryBuilder but() {
        return aCountry().withId(id).withCountryName(countryName).withIso(iso).withEuMember(euMember);
    }

    public Country build() {
        Country country = new Country();
        country.setId(id);
        country.setCountryName(countryName);
        country.setIso(iso);
        return country;
    }
}
