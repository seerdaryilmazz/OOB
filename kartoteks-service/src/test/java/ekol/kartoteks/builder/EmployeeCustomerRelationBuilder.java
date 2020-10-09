package ekol.kartoteks.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.kartoteks.domain.EmployeeCustomerRelation;

/**
 * Created by kilimci on 22/11/16.
 */
public final class EmployeeCustomerRelationBuilder {
    private Long id;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private EmployeeCustomerRelationBuilder() {
    }

    public static EmployeeCustomerRelationBuilder anEmployeeCustomerRelation() {
        return new EmployeeCustomerRelationBuilder();
    }

    public EmployeeCustomerRelationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EmployeeCustomerRelationBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public EmployeeCustomerRelationBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EmployeeCustomerRelationBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public EmployeeCustomerRelationBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public EmployeeCustomerRelationBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public EmployeeCustomerRelationBuilder but() {
        return anEmployeeCustomerRelation().withId(id).withCode(code).withName(name).withDeleted(deleted).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy);
    }

    public EmployeeCustomerRelation build() {
        EmployeeCustomerRelation employeeCustomerRelation = new EmployeeCustomerRelation();
        employeeCustomerRelation.setId(id);
        employeeCustomerRelation.setCode(code);
        employeeCustomerRelation.setName(name);
        employeeCustomerRelation.setDeleted(deleted);
        employeeCustomerRelation.setLastUpdated(lastUpdated);
        employeeCustomerRelation.setLastUpdatedBy(lastUpdatedBy);
        return employeeCustomerRelation;
    }
}
