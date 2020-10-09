package ekol.kartoteks.builder;

import ekol.hibernate5.domain.embeddable.DateWindow;
import ekol.kartoteks.domain.CompanyEmployeeRelation;
import ekol.kartoteks.domain.CompanyRole;
import ekol.kartoteks.domain.EmployeeCustomerRelation;

/**
 * Created by kilimci on 14/10/16.
 */
public final class CompanyEmployeeRelationBuilder {
    private Long id;
    private CompanyRole companyRole;
    private String employeeAccount;
    private EmployeeCustomerRelation relation;
    private DateWindow validDates;
    private boolean deleted;

    private CompanyEmployeeRelationBuilder() {
    }

    public static CompanyEmployeeRelationBuilder aCompanyEmployeeRelation() {
        return new CompanyEmployeeRelationBuilder();
    }

    public CompanyEmployeeRelationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyEmployeeRelationBuilder withCompanyRole(CompanyRole companyRole) {
        this.companyRole = companyRole;
        return this;
    }

    public CompanyEmployeeRelationBuilder withEmployeeAccount(String employeeAccount) {
        this.employeeAccount = employeeAccount;
        return this;
    }

    public CompanyEmployeeRelationBuilder withRelation(EmployeeCustomerRelation relation) {
        this.relation = relation;
        return this;
    }

    public CompanyEmployeeRelationBuilder withValidDates(DateWindow validDates) {
        this.validDates = validDates;
        return this;
    }

    public CompanyEmployeeRelationBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanyEmployeeRelationBuilder but() {
        return aCompanyEmployeeRelation().withId(id).withCompanyRole(companyRole).withEmployeeAccount(employeeAccount).withRelation(relation).withValidDates(validDates).withDeleted(deleted);
    }

    public CompanyEmployeeRelation build() {
        CompanyEmployeeRelation companyEmployeeRelation = new CompanyEmployeeRelation();
        companyEmployeeRelation.setId(id);
        companyEmployeeRelation.setCompanyRole(companyRole);
        companyEmployeeRelation.setEmployeeAccount(employeeAccount);
        companyEmployeeRelation.setRelation(relation);
        companyEmployeeRelation.setValidDates(validDates);
        companyEmployeeRelation.setDeleted(deleted);
        return companyEmployeeRelation;
    }
}
