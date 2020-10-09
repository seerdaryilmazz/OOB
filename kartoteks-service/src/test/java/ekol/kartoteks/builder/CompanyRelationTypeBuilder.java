package ekol.kartoteks.builder;

import ekol.kartoteks.domain.CompanyRelationType;

/**
 * Created by kilimci on 17/10/16.
 */
public class CompanyRelationTypeBuilder {

    private Long id;
    private String code;
    private String name;
    private boolean deleted;

    private CompanyRelationTypeBuilder() {
    }

    public static CompanyRelationTypeBuilder aCompanyRelationType() {
        return new CompanyRelationTypeBuilder();
    }

    public CompanyRelationTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    public CompanyRelationTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }
    public CompanyRelationTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public CompanyRelationTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanyRelationTypeBuilder but() {
        return aCompanyRelationType().withId(id).withName(name).withCode(code).withDeleted(deleted);
    }

    public CompanyRelationType build() {
        CompanyRelationType CompanyRelationType = new CompanyRelationType();
        CompanyRelationType.setId(id);
        CompanyRelationType.setName(name);
        CompanyRelationType.setCode(code);
        CompanyRelationType.setDeleted(deleted);
        return CompanyRelationType;
    }
    
}
