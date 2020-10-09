package ekol.kartoteks.builder;

import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyContact;

/**
 * Created by kilimci on 14/10/16.
 */
public final class CompanyContactBuilder {
    private boolean deleted;
    private boolean active;
    private Long id ;
    private Company company;
    private String firstName;
    private String lastName;

    private CompanyContactBuilder() {
    }

    public static CompanyContactBuilder aCompanyContact() {
        return new CompanyContactBuilder();
    }

    public CompanyContactBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanyContactBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public CompanyContactBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyContactBuilder withCompany(Company company) {
        this.company = company;
        return this;
    }

    public CompanyContactBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CompanyContactBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CompanyContactBuilder but() {
        return aCompanyContact().withDeleted(deleted).withActive(active).withId(id).withCompany(company).withFirstName(firstName).withLastName(lastName);
    }

    public CompanyContact build() {
        CompanyContact companyContact = new CompanyContact();
        companyContact.setDeleted(deleted);
        companyContact.setActive(active);
        companyContact.setId(id);
        companyContact.setCompany(company);
        companyContact.setFirstName(firstName);
        companyContact.setLastName(lastName);
        return companyContact;
    }
}
