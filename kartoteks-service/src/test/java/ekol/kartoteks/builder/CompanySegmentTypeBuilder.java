package ekol.kartoteks.builder;

import ekol.kartoteks.domain.CompanySegmentType;

/**
 * Created by kilimci on 17/10/16.
 */
public class CompanySegmentTypeBuilder {
    private Long id;
    private String code;
    private String name;
    private boolean deleted;

    private CompanySegmentTypeBuilder() {
    }

    public static CompanySegmentTypeBuilder aCompanySegmentType() {
        return new CompanySegmentTypeBuilder();
    }

    public CompanySegmentTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    public CompanySegmentTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }
    public CompanySegmentTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public CompanySegmentTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanySegmentTypeBuilder but() {
        return aCompanySegmentType().withId(id).withName(name).withCode(code).withDeleted(deleted);
    }

    public CompanySegmentType build() {
        CompanySegmentType companySegmentType = new CompanySegmentType();
        companySegmentType.setId(id);
        companySegmentType.setName(name);
        companySegmentType.setCode(code);
        companySegmentType.setDeleted(deleted);
        return companySegmentType;
    }
}
