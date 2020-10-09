package ekol.kartoteks.builder;

import ekol.hibernate5.domain.embeddable.DateWindow;
import ekol.kartoteks.domain.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kilimci on 14/10/16.
 */
public final class CompanyRoleBuilder {
    private Long id;
    private Company company;
    private DateWindow dateRange;
    private BusinessSegmentType segmentType;
    private CompanyRoleType roleType;
    private Set<CompanyEmployeeRelation> employeeRelations = new HashSet<>();
    private boolean deleted;

    private CompanyRoleBuilder() {
    }

    public static CompanyRoleBuilder aCompanyRole() {
        return new CompanyRoleBuilder();
    }

    public CompanyRoleBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyRoleBuilder withCompany(Company company) {
        this.company = company;
        return this;
    }

    public CompanyRoleBuilder withDateRange(DateWindow dateRange) {
        this.dateRange = dateRange;
        return this;
    }

    public CompanyRoleBuilder withSegmentType(BusinessSegmentType segmentType) {
        this.segmentType = segmentType;
        return this;
    }

    public CompanyRoleBuilder withRoleType(CompanyRoleType roleType) {
        this.roleType = roleType;
        return this;
    }

    public CompanyRoleBuilder withEmployeeRelations(Set<CompanyEmployeeRelation> employeeRelations) {
        this.employeeRelations = employeeRelations;
        return this;
    }

    public CompanyRoleBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanyRoleBuilder but() {
        return aCompanyRole().withId(id).withCompany(company).withDateRange(dateRange).withSegmentType(segmentType).withRoleType(roleType).withEmployeeRelations(employeeRelations).withDeleted(deleted);
    }

    public CompanyRole build() {
        CompanyRole companyRole = new CompanyRole();
        companyRole.setId(id);
        companyRole.setCompany(company);
        companyRole.setDateRange(dateRange);
        companyRole.setSegmentType(segmentType);
        companyRole.setRoleType(roleType);
        companyRole.setEmployeeRelations(employeeRelations);
        companyRole.setDeleted(deleted);
        return companyRole;
    }
}
