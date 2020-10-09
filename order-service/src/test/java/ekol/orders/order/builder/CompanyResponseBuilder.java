package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.response.kartoteks.CompanyResponse;
import ekol.orders.order.domain.dto.response.kartoteks.Country;

public final class CompanyResponseBuilder {
    private Long id ;
    private String name;
    private Country country;

    private CompanyResponseBuilder() {
    }

    public static CompanyResponseBuilder aCompanyResponse() {
        return new CompanyResponseBuilder();
    }

    public CompanyResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyResponseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CompanyResponseBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public CompanyResponse build() {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(id);
        companyResponse.setName(name);
        companyResponse.setCountry(country);
        return companyResponse;
    }
}
