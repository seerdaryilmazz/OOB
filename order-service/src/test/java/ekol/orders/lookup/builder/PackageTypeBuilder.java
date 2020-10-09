package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.PackageGroup;
import ekol.orders.lookup.domain.PackageType;

public final class PackageTypeBuilder {

    private Long id;
    private boolean hasRestriction;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private PackageGroup packageGroup;

    private PackageTypeBuilder() {
    }

    public static PackageTypeBuilder aPackageType() {
        return new PackageTypeBuilder();
    }

    public PackageTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PackageTypeBuilder withHasRestriction(boolean hasRestriction) {
        this.hasRestriction = hasRestriction;
        return this;
    }

    public PackageTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public PackageTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PackageTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public PackageTypeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public PackageTypeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public PackageTypeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public PackageTypeBuilder withPackageGroup(PackageGroup packageGroup) {
        this.packageGroup = packageGroup;
        return this;
    }

    public PackageType build() {
        PackageType packageType = new PackageType();
        packageType.setId(id);
        packageType.setHasRestriction(hasRestriction);
        packageType.setCode(code);
        packageType.setName(name);
        packageType.setDeleted(deleted);
        packageType.setDeletedAt(deletedAt);
        packageType.setLastUpdated(lastUpdated);
        packageType.setLastUpdatedBy(lastUpdatedBy);
        packageType.setPackageGroup(packageGroup);
        return packageType;
    }
}
