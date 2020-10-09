package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.AdrPackageType;

public final class AdrPackageTypeBuilder {
    private String code;
    private String name;
    private boolean deleted;
    private Long id;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private UtcDateTime deletedAt;

    private AdrPackageTypeBuilder() {
    }

    public static AdrPackageTypeBuilder anAdrPackageType() {
        return new AdrPackageTypeBuilder();
    }

    public AdrPackageTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public AdrPackageTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AdrPackageTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public AdrPackageTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AdrPackageTypeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public AdrPackageTypeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public AdrPackageTypeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public AdrPackageTypeBuilder but() {
        return anAdrPackageType().withCode(code).withName(name).withDeleted(deleted).withId(id).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy).withDeletedAt(deletedAt);
    }

    public AdrPackageType build() {
        AdrPackageType adrPackageType = new AdrPackageType();
        adrPackageType.setCode(code);
        adrPackageType.setName(name);
        adrPackageType.setDeleted(deleted);
        adrPackageType.setId(id);
        adrPackageType.setLastUpdated(lastUpdated);
        adrPackageType.setLastUpdatedBy(lastUpdatedBy);
        adrPackageType.setDeletedAt(deletedAt);
        return adrPackageType;
    }
}
