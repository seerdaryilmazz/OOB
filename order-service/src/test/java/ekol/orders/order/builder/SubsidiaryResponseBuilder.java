package ekol.orders.order.builder;

import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.response.SubsidiaryResponse;

public final class SubsidiaryResponseBuilder {
    private Long id;
    private String name;
    private IdNamePair defaultInvoiceCompany;

    private SubsidiaryResponseBuilder() {
    }

    public static SubsidiaryResponseBuilder aSubsidiaryResponse() {
        return new SubsidiaryResponseBuilder();
    }

    public SubsidiaryResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public SubsidiaryResponseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SubsidiaryResponseBuilder withDefaultInvoiceCompany(IdNamePair defaultInvoiceCompany) {
        this.defaultInvoiceCompany = defaultInvoiceCompany;
        return this;
    }

    public SubsidiaryResponseBuilder but() {
        return aSubsidiaryResponse().withId(id).withName(name).withDefaultInvoiceCompany(defaultInvoiceCompany);
    }

    public SubsidiaryResponse build() {
        SubsidiaryResponse subsidiaryResponse = new SubsidiaryResponse();
        subsidiaryResponse.setId(id);
        subsidiaryResponse.setName(name);
        subsidiaryResponse.setDefaultInvoiceCompany(defaultInvoiceCompany);
        return subsidiaryResponse;
    }
}
