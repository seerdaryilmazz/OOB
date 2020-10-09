package ekol.agreement.domain.model.agreement;

import ekol.agreement.domain.dto.agreement.AgreementJson;
import ekol.agreement.domain.enumaration.AgreementStatus;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.enumaration.RenewalDateType;
import ekol.agreement.domain.model.*;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Agreement")
@SequenceGenerator(name = "SEQ_AGREEMENT", sequenceName = "SEQ_AGREEMENT")
@Getter
@Setter
@NoArgsConstructor
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Agreement extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AGREEMENT")
    private Long id;

    @Column(name = "AGREEMENT_NUMBER")
    private Long number;

    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createdAt"))
    })
    private UtcDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @Enumerated(EnumType.STRING)
    private AgreementType type;

    @Enumerated(EnumType.STRING)
    private AgreementStatus status;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "ACCOUNT_ID")),
            @AttributeOverride(name = "name", column = @Column(name = "ACCOUNT_NAME"))})
    private IdNamePair account;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "Agreement_Service_Area", joinColumns = @JoinColumn(name = "agreement_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "code", column = @Column(name = "CODE")),
            @AttributeOverride(name = "name", column = @Column(name = "NAME"))
    })
    private Set<CodeNamePair> serviceAreas = new HashSet<>();

    private Integer renewalLength;

    @Enumerated(EnumType.STRING)
    private RenewalDateType renewalDateType;

    private Integer autoRenewalLength;

    @Enumerated(EnumType.STRING)
    private RenewalDateType autoRenewalDateType;

    private LocalDate autoRenewalDate;

    @OneToMany(mappedBy="agreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private Set<KpiInfo> kpiInfos = new HashSet<>();

    @OneToMany(mappedBy="agreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private Set<InsuranceInfo> insuranceInfos = new HashSet<>();

    @OneToMany(mappedBy="agreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private Set<OwnerInfo> ownerInfos = new HashSet<>();

    @OneToMany(mappedBy="agreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private Set<UnitPrice> unitPrices = new HashSet<>();

    @OneToMany(mappedBy="agreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private Set<PriceAdaptationModel> priceAdaptationModels = new HashSet<>();

    @OneToMany(mappedBy="agreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private Set<LetterOfGuarentee> letterOfGuarentees = new HashSet<>();

    @OneToMany(mappedBy="agreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private Set<SignatureInfo> signatureInfos = new HashSet<>();

    @Embedded
    private FinancialInfo financialInfo;

    @Embedded
    private LegalInfo legalInfo;

    @Transient
    private List<Note> notes = new ArrayList<>();

    @Transient
    private List<Document> documents = new ArrayList<>();

    public abstract AgreementJson toJson();

    @Override
    @PrePersist
    public void prePersist() {
        if (null == createdAt && null == id) {
            createdAt = new UtcDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        }
        super.prePersist();
    }

    public void setInsuranceInfos(Set<InsuranceInfo> insuranceInfos) {
        this.insuranceInfos.clear();
        if (insuranceInfos != null) {
            this.insuranceInfos.addAll(insuranceInfos);
        }
    }

    public void setUnitPrices(Set<UnitPrice> unitPrices) {
        this.unitPrices.clear();
        if (unitPrices != null) {
            this.unitPrices.addAll(unitPrices);
        }
    }

    public void setPriceAdaptationModels(Set<PriceAdaptationModel> priceAdaptationModels) {
        this.priceAdaptationModels.clear();
        if (priceAdaptationModels != null) {
            this.priceAdaptationModels.addAll(priceAdaptationModels);
        }
    }

    public void setKpiInfos(Set<KpiInfo> kpiInfos) {
        this.kpiInfos.clear();
        if (kpiInfos != null) {
            this.kpiInfos.addAll(kpiInfos);
        }
    }

    public void setSignatureInfos(Set<SignatureInfo> signatureInfos) {
        this.signatureInfos.clear();
        if (signatureInfos != null) {
            this.signatureInfos.addAll(signatureInfos);
        }
    }

    public void setOwnerInfos(Set<OwnerInfo> ownerInfos) {
        this.ownerInfos.clear();
        if (ownerInfos != null) {
            this.ownerInfos.addAll(ownerInfos);
        }
    }

    public void setLetterOfGuarentees(Set<LetterOfGuarentee> letterOfGuarentees) {
        this.letterOfGuarentees.clear();
        if (letterOfGuarentees != null) {
            this.letterOfGuarentees.addAll(letterOfGuarentees);
        }
    }
}
