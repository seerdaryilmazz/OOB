package ekol.kartoteks.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.kartoteks.domain.CompanyRoleType;

/**
 * Created by kilimci on 22/11/16.
 */
public final class CompanyRoleTypeBuilder {
    private Long id;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private CompanyRoleTypeBuilder() {
    }

    public static CompanyRoleTypeBuilder aCompanyRoleType() {
        return new CompanyRoleTypeBuilder();
    }

    public CompanyRoleTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyRoleTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public CompanyRoleTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CompanyRoleTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanyRoleTypeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public CompanyRoleTypeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public CompanyRoleTypeBuilder but() {
        return aCompanyRoleType().withId(id).withCode(code).withName(name).withDeleted(deleted).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy);
    }

    public CompanyRoleType build() {
        CompanyRoleType companyRoleType = new CompanyRoleType();
        companyRoleType.setId(id);
        companyRoleType.setCode(code);
        companyRoleType.setName(name);
        companyRoleType.setDeleted(deleted);
        companyRoleType.setLastUpdated(lastUpdated);
        companyRoleType.setLastUpdatedBy(lastUpdatedBy);
        return companyRoleType;
    }
}
