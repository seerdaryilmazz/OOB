package ekol.orders.lookup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "AdrClassDetails")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdrClassDetails extends BaseEntity{

    @Id
    @SequenceGenerator(name = "SEQ_ADR_CODE_DETAILS", sequenceName = "SEQ_ADR_CODE_DETAILS")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADR_CODE_DETAILS")
    private Long id;

    @Column
    private String unNumber;

    @Column
    private String description;

    @Column
    private String adrClass;

    @Column
    private String classificationCode;

    @Column
    private String packingGroup;

    @Column
    private String labels;

    @Column
    private String specialProvision;

    @Column
    private String limitedQuantity;

    @Column
    private String expectedQuantity;

    @Column
    private String packingInstructions;

    @Column
    private String specialPackingProvisions;

    @Column
    private String mixedPackingProvisions;

    @Column
    private String containerInstructions;

    @Column
    private String containerSpecialProvisions;

    @Column
    private String adrTankCode;

    @Column
    private String adrTankSpecialProvisions;

    @Column
    private String vehicleTankCarriage;

    @Column
    private String transportTunnelCategory;

    @Column
    private String specialProvisionsPackages;

    @Column
    private String specialProvisionsBulk;

    @Column
    private String specialProvisionsHandling;

    @Column
    private String specialProvisionsOperation;

    @Column
    private String hazardIdentification;

    @Column
    @Enumerated(EnumType.STRING)
    private AdrClassDetailsVersion adrVersion;

    public static AdrClassDetails with(Long id){
        AdrClassDetails adrClassDetails = new AdrClassDetails();
        adrClassDetails.setId(id);
        return adrClassDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnNumber() {
        return unNumber;
    }

    public void setUnNumber(String unNumber) {
        this.unNumber = unNumber;
    }

    public String getPackingGroup() {
        return packingGroup;
    }

    public void setPackingGroup(String packingGroup) {
        this.packingGroup = packingGroup;
    }

    public String getClassificationCode() {
        return classificationCode;
    }

    public void setClassificationCode(String classificationCode) {
        this.classificationCode = classificationCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdrClass() {
        return adrClass;
    }

    public void setAdrClass(String adrClass) {
        this.adrClass = adrClass;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getSpecialProvision() {
        return specialProvision;
    }

    public void setSpecialProvision(String specialProvision) {
        this.specialProvision = specialProvision;
    }

    public String getLimitedQuantity() {
        return limitedQuantity;
    }

    public void setLimitedQuantity(String limitedQuantity) {
        this.limitedQuantity = limitedQuantity;
    }

    public String getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(String expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public String getPackingInstructions() {
        return packingInstructions;
    }

    public void setPackingInstructions(String packingInstructions) {
        this.packingInstructions = packingInstructions;
    }

    public String getSpecialPackingProvisions() {
        return specialPackingProvisions;
    }

    public void setSpecialPackingProvisions(String specialPackingProvisions) {
        this.specialPackingProvisions = specialPackingProvisions;
    }

    public String getMixedPackingProvisions() {
        return mixedPackingProvisions;
    }

    public void setMixedPackingProvisions(String mixedPackingProvisions) {
        this.mixedPackingProvisions = mixedPackingProvisions;
    }

    public String getContainerInstructions() {
        return containerInstructions;
    }

    public void setContainerInstructions(String containerInstructions) {
        this.containerInstructions = containerInstructions;
    }

    public String getContainerSpecialProvisions() {
        return containerSpecialProvisions;
    }

    public void setContainerSpecialProvisions(String containerSpecialProvisions) {
        this.containerSpecialProvisions = containerSpecialProvisions;
    }

    public String getAdrTankCode() {
        return adrTankCode;
    }

    public void setAdrTankCode(String adrTankCode) {
        this.adrTankCode = adrTankCode;
    }

    public String getAdrTankSpecialProvisions() {
        return adrTankSpecialProvisions;
    }

    public void setAdrTankSpecialProvisions(String adrTankSpecialProvisions) {
        this.adrTankSpecialProvisions = adrTankSpecialProvisions;
    }

    public String getVehicleTankCarriage() {
        return vehicleTankCarriage;
    }

    public void setVehicleTankCarriage(String vehicleTankCarriage) {
        this.vehicleTankCarriage = vehicleTankCarriage;
    }

    public String getTransportTunnelCategory() {
        return transportTunnelCategory;
    }

    public void setTransportTunnelCategory(String transportTunnelCategory) {
        this.transportTunnelCategory = transportTunnelCategory;
    }

    public String getSpecialProvisionsPackages() {
        return specialProvisionsPackages;
    }

    public void setSpecialProvisionsPackages(String specialProvisionsPackages) {
        this.specialProvisionsPackages = specialProvisionsPackages;
    }

    public String getSpecialProvisionsBulk() {
        return specialProvisionsBulk;
    }

    public void setSpecialProvisionsBulk(String specialProvisionsBulk) {
        this.specialProvisionsBulk = specialProvisionsBulk;
    }

    public String getSpecialProvisionsHandling() {
        return specialProvisionsHandling;
    }

    public void setSpecialProvisionsHandling(String specialProvisionsHandling) {
        this.specialProvisionsHandling = specialProvisionsHandling;
    }

    public String getSpecialProvisionsOperation() {
        return specialProvisionsOperation;
    }

    public void setSpecialProvisionsOperation(String specialProvisionsOperation) {
        this.specialProvisionsOperation = specialProvisionsOperation;
    }

    public String getHazardIdentification() {
        return hazardIdentification;
    }

    public void setHazardIdentification(String hazardIdentification) {
        this.hazardIdentification = hazardIdentification;
    }

    public AdrClassDetailsVersion getAdrVersion() {
        return adrVersion;
    }

    public void setAdrVersion(AdrClassDetailsVersion adrVersion) {
        this.adrVersion = adrVersion;
    }
}
