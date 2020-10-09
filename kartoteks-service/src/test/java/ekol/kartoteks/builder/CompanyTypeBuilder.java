package ekol.kartoteks.builder;

import ekol.kartoteks.domain.CompanyType;

/**
 * Created by kilimci on 17/10/16.
 */
public final class CompanyTypeBuilder {
    private Long id;
    private String code;
    private String name;
    private boolean deleted;

    private CompanyTypeBuilder() {
    }

    public static CompanyTypeBuilder aCompanyType() {
        return new CompanyTypeBuilder();
    }

    public CompanyTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    public CompanyTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }
    public CompanyTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public CompanyTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanyTypeBuilder but() {
        return aCompanyType().withId(id).withName(name).withCode(code).withDeleted(deleted);
    }

    public CompanyType build() {
        CompanyType companyType = new CompanyType();
        companyType.setId(id);
        companyType.setName(name);
        companyType.setCode(code);
        companyType.setDeleted(deleted);
        return companyType;
    }
}
