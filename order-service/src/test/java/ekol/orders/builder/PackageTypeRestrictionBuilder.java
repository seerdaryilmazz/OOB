package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.BigDecimalRange;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.transportOrder.domain.PackageTypeRestriction;


public final class PackageTypeRestrictionBuilder {

    private Long id;
    private PackageType packageType;
    private BigDecimalRange grossWeightRangeInKilograms;
    private BigDecimalRange netWeightRangeInKilograms;
    private BigDecimalRange volumeRangeInCubicMeters;
    private BigDecimalRange widthRangeInCentimeters;
    private BigDecimalRange lengthRangeInCentimeters;
    private BigDecimalRange heightRangeInCentimeters;
    private BigDecimalRange ldmRange;
    private boolean stackable;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private PackageTypeRestrictionBuilder() {
    }

    public static PackageTypeRestrictionBuilder aPackageTypeRestriction() {
        return new PackageTypeRestrictionBuilder();
    }

    public PackageTypeRestrictionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PackageTypeRestrictionBuilder withPackageType(PackageType packageType) {
        this.packageType = packageType;
        return this;
    }

    public PackageTypeRestrictionBuilder withGrossWeightRangeInKilograms(BigDecimalRange grossWeightRangeInKilograms) {
        this.grossWeightRangeInKilograms = grossWeightRangeInKilograms;
        return this;
    }

    public PackageTypeRestrictionBuilder withNetWeightRangeInKilograms(BigDecimalRange netWeightRangeInKilograms) {
        this.netWeightRangeInKilograms = netWeightRangeInKilograms;
        return this;
    }

    public PackageTypeRestrictionBuilder withVolumeRangeInCubicMeters(BigDecimalRange volumeRangeInCubicMeters) {
        this.volumeRangeInCubicMeters = volumeRangeInCubicMeters;
        return this;
    }

    public PackageTypeRestrictionBuilder withWidthRangeInCentimeters(BigDecimalRange widthRangeInCentimeters) {
        this.widthRangeInCentimeters = widthRangeInCentimeters;
        return this;
    }

    public PackageTypeRestrictionBuilder withLengthRangeInCentimeters(BigDecimalRange lengthRangeInCentimeters) {
        this.lengthRangeInCentimeters = lengthRangeInCentimeters;
        return this;
    }

    public PackageTypeRestrictionBuilder withHeightRangeInCentimeters(BigDecimalRange heightRangeInCentimeters) {
        this.heightRangeInCentimeters = heightRangeInCentimeters;
        return this;
    }

    public PackageTypeRestrictionBuilder withLdmRange(BigDecimalRange ldmRange) {
        this.ldmRange = ldmRange;
        return this;
    }

    public PackageTypeRestrictionBuilder withStackable(boolean stackable) {
        this.stackable = stackable;
        return this;
    }

    public PackageTypeRestrictionBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public PackageTypeRestrictionBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public PackageTypeRestrictionBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public PackageTypeRestrictionBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public PackageTypeRestriction build() {
        PackageTypeRestriction packageTypeRestriction = new PackageTypeRestriction();
        packageTypeRestriction.setId(id);
        packageTypeRestriction.setPackageType(packageType);
        packageTypeRestriction.setGrossWeightRangeInKilograms(grossWeightRangeInKilograms);
        packageTypeRestriction.setNetWeightRangeInKilograms(netWeightRangeInKilograms);
        packageTypeRestriction.setVolumeRangeInCubicMeters(volumeRangeInCubicMeters);
        packageTypeRestriction.setWidthRangeInCentimeters(widthRangeInCentimeters);
        packageTypeRestriction.setLengthRangeInCentimeters(lengthRangeInCentimeters);
        packageTypeRestriction.setHeightRangeInCentimeters(heightRangeInCentimeters);
        packageTypeRestriction.setLdmRange(ldmRange);
        packageTypeRestriction.setStackable(stackable);
        packageTypeRestriction.setDeleted(deleted);
        packageTypeRestriction.setDeletedAt(deletedAt);
        packageTypeRestriction.setLastUpdated(lastUpdated);
        packageTypeRestriction.setLastUpdatedBy(lastUpdatedBy);
        return packageTypeRestriction;
    }
}
