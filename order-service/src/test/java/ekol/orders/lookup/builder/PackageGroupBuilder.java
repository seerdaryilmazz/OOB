package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.PackageGroup;
import ekol.orders.lookup.domain.PackageType;

import java.util.HashSet;
import java.util.Set;

public final class PackageGroupBuilder {
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime lastUpdated;
    private Long id;
    private String lastUpdatedBy;
    private Set<PackageType> packageTypes = new HashSet<>();
    private UtcDateTime deletedAt;

    private PackageGroupBuilder() {
    }

    public static PackageGroupBuilder aPackageGroup() {
        return new PackageGroupBuilder();
    }

    public PackageGroupBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public PackageGroupBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PackageGroupBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public PackageGroupBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public PackageGroupBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PackageGroupBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public PackageGroupBuilder withPackageTypes(Set<PackageType> packageTypes) {
        this.packageTypes = packageTypes;
        return this;
    }

    public PackageGroupBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public PackageGroupBuilder but() {
        return aPackageGroup().withCode(code).withName(name).withDeleted(deleted).withLastUpdated(lastUpdated).withId(id).withLastUpdatedBy(lastUpdatedBy).withPackageTypes(packageTypes).withDeletedAt(deletedAt);
    }

    public PackageGroup build() {
        PackageGroup packageGroup = new PackageGroup();
        packageGroup.setCode(code);
        packageGroup.setName(name);
        packageGroup.setDeleted(deleted);
        packageGroup.setLastUpdated(lastUpdated);
        packageGroup.setId(id);
        packageGroup.setLastUpdatedBy(lastUpdatedBy);
        packageGroup.setPackageTypes(packageTypes);
        packageGroup.setDeletedAt(deletedAt);
        return packageGroup;
    }
}
