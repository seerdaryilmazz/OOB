package ekol.kartoteks.builder;

import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.domain.PostalAddress;

/**
 * Created by kilimci on 14/10/16.
 */
public final class CompanyLocationBuilder {
    private Long id ;
    private String name;
    private Company company;
    private PostalAddress postaladdress;
    private boolean active;
    private boolean deleted;

    private CompanyLocationBuilder() {
    }

    public static CompanyLocationBuilder aCompanyLocation() {
        return new CompanyLocationBuilder();
    }

    public CompanyLocationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyLocationBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CompanyLocationBuilder withCompany(Company company) {
        this.company = company;
        return this;
    }

    public CompanyLocationBuilder withPostaladdress(PostalAddress postaladdress) {
        this.postaladdress = postaladdress;
        return this;
    }

    public CompanyLocationBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public CompanyLocationBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanyLocationBuilder but() {
        return aCompanyLocation().withId(id).withName(name).withCompany(company).withPostaladdress(postaladdress).withActive(active).withDeleted(deleted);
    }

    public CompanyLocation build() {
        CompanyLocation companyLocation = new CompanyLocation();
        companyLocation.setId(id);
        companyLocation.setName(name);
        companyLocation.setCompany(company);
        companyLocation.setPostaladdress(postaladdress);
        companyLocation.setActive(active);
        companyLocation.setDeleted(deleted);
        return companyLocation;
    }
}
