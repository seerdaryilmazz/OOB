package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.AdrClassDetails;
import ekol.orders.lookup.domain.AdrClassDetailsVersion;

public final class AdrClassDetailsBuilder {
    private boolean deleted;
    private Long id;
    private String unNumber;
    private String description;
    private UtcDateTime lastUpdated;
    private String adrClass;
    private String classificationCode;
    private String lastUpdatedBy;
    private String packingGroup;
    private String labels;
    private String specialProvision;
    private String limitedQuantity;
    private UtcDateTime deletedAt;
    private String expectedQuantity;
    private String packingInstructions;
    private String specialPackingProvisions;
    private String mixedPackingProvisions;
    private String containerInstructions;
    private String containerSpecialProvisions;
    private String adrTankCode;
    private String adrTankSpecialProvisions;
    private String vehicleTankCarriage;
    private String transportTunnelCategory;
    private String specialProvisionsPackages;
    private String specialProvisionsBulk;
    private String specialProvisionsHandling;
    private String specialProvisionsOperation;
    private String hazardIdentification;
    private AdrClassDetailsVersion adrVersion;

    private AdrClassDetailsBuilder() {
    }

    public static AdrClassDetailsBuilder anAdrClassDetails() {
        return new AdrClassDetailsBuilder();
    }

    public AdrClassDetailsBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public AdrClassDetailsBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AdrClassDetailsBuilder withUnNumber(String unNumber) {
        this.unNumber = unNumber;
        return this;
    }

    public AdrClassDetailsBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AdrClassDetailsBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public AdrClassDetailsBuilder withAdrClass(String adrClass) {
        this.adrClass = adrClass;
        return this;
    }

    public AdrClassDetailsBuilder withClassificationCode(String classificationCode) {
        this.classificationCode = classificationCode;
        return this;
    }

    public AdrClassDetailsBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public AdrClassDetailsBuilder withPackingGroup(String packingGroup) {
        this.packingGroup = packingGroup;
        return this;
    }

    public AdrClassDetailsBuilder withLabels(String labels) {
        this.labels = labels;
        return this;
    }

    public AdrClassDetailsBuilder withSpecialProvision(String specialProvision) {
        this.specialProvision = specialProvision;
        return this;
    }

    public AdrClassDetailsBuilder withLimitedQuantity(String limitedQuantity) {
        this.limitedQuantity = limitedQuantity;
        return this;
    }

    public AdrClassDetailsBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public AdrClassDetailsBuilder withExpectedQuantity(String expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
        return this;
    }

    public AdrClassDetailsBuilder withPackingInstructions(String packingInstructions) {
        this.packingInstructions = packingInstructions;
        return this;
    }

    public AdrClassDetailsBuilder withSpecialPackingProvisions(String specialPackingProvisions) {
        this.specialPackingProvisions = specialPackingProvisions;
        return this;
    }

    public AdrClassDetailsBuilder withMixedPackingProvisions(String mixedPackingProvisions) {
        this.mixedPackingProvisions = mixedPackingProvisions;
        return this;
    }

    public AdrClassDetailsBuilder withContainerInstructions(String containerInstructions) {
        this.containerInstructions = containerInstructions;
        return this;
    }

    public AdrClassDetailsBuilder withContainerSpecialProvisions(String containerSpecialProvisions) {
        this.containerSpecialProvisions = containerSpecialProvisions;
        return this;
    }

    public AdrClassDetailsBuilder withAdrTankCode(String adrTankCode) {
        this.adrTankCode = adrTankCode;
        return this;
    }

    public AdrClassDetailsBuilder withAdrTankSpecialProvisions(String adrTankSpecialProvisions) {
        this.adrTankSpecialProvisions = adrTankSpecialProvisions;
        return this;
    }

    public AdrClassDetailsBuilder withVehicleTankCarriage(String vehicleTankCarriage) {
        this.vehicleTankCarriage = vehicleTankCarriage;
        return this;
    }

    public AdrClassDetailsBuilder withTransportTunnelCategory(String transportTunnelCategory) {
        this.transportTunnelCategory = transportTunnelCategory;
        return this;
    }

    public AdrClassDetailsBuilder withSpecialProvisionsPackages(String specialProvisionsPackages) {
        this.specialProvisionsPackages = specialProvisionsPackages;
        return this;
    }

    public AdrClassDetailsBuilder withSpecialProvisionsBulk(String specialProvisionsBulk) {
        this.specialProvisionsBulk = specialProvisionsBulk;
        return this;
    }

    public AdrClassDetailsBuilder withSpecialProvisionsHandling(String specialProvisionsHandling) {
        this.specialProvisionsHandling = specialProvisionsHandling;
        return this;
    }

    public AdrClassDetailsBuilder withSpecialProvisionsOperation(String specialProvisionsOperation) {
        this.specialProvisionsOperation = specialProvisionsOperation;
        return this;
    }

    public AdrClassDetailsBuilder withHazardIdentification(String hazardIdentification) {
        this.hazardIdentification = hazardIdentification;
        return this;
    }

    public AdrClassDetailsBuilder withAdrVersion(AdrClassDetailsVersion adrVersion) {
        this.adrVersion = adrVersion;
        return this;
    }

    public AdrClassDetailsBuilder but() {
        return anAdrClassDetails().withDeleted(deleted).withId(id).withUnNumber(unNumber).withDescription(description).withLastUpdated(lastUpdated).withAdrClass(adrClass).withClassificationCode(classificationCode).withLastUpdatedBy(lastUpdatedBy).withPackingGroup(packingGroup).withLabels(labels).withSpecialProvision(specialProvision).withLimitedQuantity(limitedQuantity).withDeletedAt(deletedAt).withExpectedQuantity(expectedQuantity).withPackingInstructions(packingInstructions).withSpecialPackingProvisions(specialPackingProvisions).withMixedPackingProvisions(mixedPackingProvisions).withContainerInstructions(containerInstructions).withContainerSpecialProvisions(containerSpecialProvisions).withAdrTankCode(adrTankCode).withAdrTankSpecialProvisions(adrTankSpecialProvisions).withVehicleTankCarriage(vehicleTankCarriage).withTransportTunnelCategory(transportTunnelCategory).withSpecialProvisionsPackages(specialProvisionsPackages).withSpecialProvisionsBulk(specialProvisionsBulk).withSpecialProvisionsHandling(specialProvisionsHandling).withSpecialProvisionsOperation(specialProvisionsOperation).withHazardIdentification(hazardIdentification).withAdrVersion(adrVersion);
    }

    public AdrClassDetails build() {
        AdrClassDetails adrClassDetails = new AdrClassDetails();
        adrClassDetails.setDeleted(deleted);
        adrClassDetails.setId(id);
        adrClassDetails.setUnNumber(unNumber);
        adrClassDetails.setDescription(description);
        adrClassDetails.setLastUpdated(lastUpdated);
        adrClassDetails.setAdrClass(adrClass);
        adrClassDetails.setClassificationCode(classificationCode);
        adrClassDetails.setLastUpdatedBy(lastUpdatedBy);
        adrClassDetails.setPackingGroup(packingGroup);
        adrClassDetails.setLabels(labels);
        adrClassDetails.setSpecialProvision(specialProvision);
        adrClassDetails.setLimitedQuantity(limitedQuantity);
        adrClassDetails.setDeletedAt(deletedAt);
        adrClassDetails.setExpectedQuantity(expectedQuantity);
        adrClassDetails.setPackingInstructions(packingInstructions);
        adrClassDetails.setSpecialPackingProvisions(specialPackingProvisions);
        adrClassDetails.setMixedPackingProvisions(mixedPackingProvisions);
        adrClassDetails.setContainerInstructions(containerInstructions);
        adrClassDetails.setContainerSpecialProvisions(containerSpecialProvisions);
        adrClassDetails.setAdrTankCode(adrTankCode);
        adrClassDetails.setAdrTankSpecialProvisions(adrTankSpecialProvisions);
        adrClassDetails.setVehicleTankCarriage(vehicleTankCarriage);
        adrClassDetails.setTransportTunnelCategory(transportTunnelCategory);
        adrClassDetails.setSpecialProvisionsPackages(specialProvisionsPackages);
        adrClassDetails.setSpecialProvisionsBulk(specialProvisionsBulk);
        adrClassDetails.setSpecialProvisionsHandling(specialProvisionsHandling);
        adrClassDetails.setSpecialProvisionsOperation(specialProvisionsOperation);
        adrClassDetails.setHazardIdentification(hazardIdentification);
        adrClassDetails.setAdrVersion(adrVersion);
        return adrClassDetails;
    }
}
