package ekol.agreement.builder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.enumaration.RenewalDateType;
import ekol.agreement.domain.enumaration.ServiceArea;
import ekol.agreement.domain.model.*;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.domain.model.agreement.AgreementLogistic;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public final class AgreementBuilder {

    private Long id;
    private Long number;
    private String name;
    private AgreementType type;
    private IdNamePair account;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<CodeNamePair> serviceAreas = new HashSet<>();
    private Integer renewalLength;
    private RenewalDateType renewalDateType;
    private Integer autoRenewalLength;
    private RenewalDateType autoRenewalDateType;
    private LocalDate autoRenewalDate;
    private Set<KpiInfo> kpiInfos = new HashSet<>();
    private Set<OwnerInfo> ownerInfos = new HashSet<>();
    private FinancialInfo financialInfo;
    private Set<LetterOfGuarentee> letterOfGuarentees = new HashSet<>();
    private Set<SignatureInfo> signatureInfos = new HashSet<>();
    private LegalInfo legalInfo;

    public static AgreementBuilder anAgreement() { return new AgreementBuilder(); }

    public AgreementBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AgreementBuilder withNumber(Long number) {
        this.number = number;
        return this;
    }

    public AgreementBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AgreementBuilder withType(AgreementType type) {
        this.type = type;
        return this;
    }

    public AgreementBuilder withAccount(IdNamePair account) {
        this.account = account;
        return this;
    }

    public AgreementBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public AgreementBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public AgreementBuilder withServiceAreas(Set<CodeNamePair> serviceAreas) {
        this.serviceAreas = serviceAreas;
        return this;
    }

    public AgreementBuilder withRenewalLength(Integer renewalLength) {
        this.renewalLength = renewalLength;
        return this;
    }

    public AgreementBuilder withRenewalDateType(RenewalDateType renewalDateType) {
        this.renewalDateType = renewalDateType;
        return this;
    }

    public AgreementBuilder withAutoRenewalLength(Integer autoRenewalLength) {
        this.autoRenewalLength = autoRenewalLength;
        return this;
    }

    public AgreementBuilder withAutoRenewalDateType(RenewalDateType autoRenewalDateType) {
        this.autoRenewalDateType = autoRenewalDateType;
        return this;
    }

    public AgreementBuilder withAutoRenewalDate(LocalDate autoRenewalDate) {
        this.autoRenewalDate = autoRenewalDate;
        return this;
    }

    public AgreementBuilder withKpiInfos(Set<KpiInfo> kpiInfos) {
        this.kpiInfos = kpiInfos;
        return this;
    }

    public AgreementBuilder withOwnerInfos(Set<OwnerInfo> ownerInfos) {
        this.ownerInfos = ownerInfos;
        return this;
    }

    public AgreementBuilder withFinancialInfo(FinancialInfo financialInfo) {
        this.financialInfo = financialInfo;
        return this;
    }

    public AgreementBuilder withLetterOfGuarentees(Set<LetterOfGuarentee> letterOfGuarentees) {
        this.letterOfGuarentees = letterOfGuarentees;
        return this;
    }

    public AgreementBuilder withLegalInfo(LegalInfo legalInfo) {
        this.legalInfo = legalInfo;
        return this;
    }

    public AgreementBuilder withSignatureInfos(Set<SignatureInfo> signatureInfos) {
        this.signatureInfos = signatureInfos;
        return this;
    }

    public AgreementLogistic build() {
        AgreementLogistic agreement = new AgreementLogistic();
        agreement.setId(id);
        agreement.setName(name);
        agreement.setType(type);
        agreement.setAccount(account);
        agreement.setStartDate(startDate);
        agreement.setEndDate(endDate);
        agreement.setServiceAreas(serviceAreas);
        agreement.setRenewalLength(renewalLength);
        agreement.setRenewalDateType(renewalDateType);
        agreement.setAutoRenewalLength(autoRenewalLength);
        agreement.setAutoRenewalDateType(autoRenewalDateType);
        agreement.setAutoRenewalDate(autoRenewalDate);
        agreement.setKpiInfos(kpiInfos);
        agreement.setOwnerInfos(ownerInfos);
        agreement.setFinancialInfo(financialInfo);
        agreement.setLetterOfGuarentees(letterOfGuarentees);
        agreement.setLegalInfo(legalInfo);
        agreement.setSignatureInfos(signatureInfos);
        return agreement;
    }
}
