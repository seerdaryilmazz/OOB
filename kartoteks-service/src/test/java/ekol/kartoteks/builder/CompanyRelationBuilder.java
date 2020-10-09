package ekol.kartoteks.builder;

import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyRelation;
import ekol.kartoteks.domain.CompanyRelationType;

/**
 * Created by kilimci on 14/10/16.
 */
public final class CompanyRelationBuilder {
    private Long id;
    private Company activeCompany;
    private Company passiveCompany;
    private CompanyRelationType relationType;
    private boolean deleted;

    private CompanyRelationBuilder() {
    }

    public static CompanyRelationBuilder aCompanyRelation() {
        return new CompanyRelationBuilder();
    }

    public CompanyRelationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyRelationBuilder withActiveCompany(Company activeCompany) {
        this.activeCompany = activeCompany;
        return this;
    }

    public CompanyRelationBuilder withPassiveCompany(Company passiveCompany) {
        this.passiveCompany = passiveCompany;
        return this;
    }

    public CompanyRelationBuilder withRelationType(CompanyRelationType relationType) {
        this.relationType = relationType;
        return this;
    }

    public CompanyRelationBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CompanyRelationBuilder but() {
        return aCompanyRelation().withId(id).withActiveCompany(activeCompany).withPassiveCompany(passiveCompany).withRelationType(relationType).withDeleted(deleted);
    }

    public CompanyRelation build() {
        CompanyRelation companyRelation = new CompanyRelation();
        companyRelation.setId(id);
        companyRelation.setActiveCompany(activeCompany);
        companyRelation.setPassiveCompany(passiveCompany);
        companyRelation.setRelationType(relationType);
        companyRelation.setDeleted(deleted);
        return companyRelation;
    }
}
