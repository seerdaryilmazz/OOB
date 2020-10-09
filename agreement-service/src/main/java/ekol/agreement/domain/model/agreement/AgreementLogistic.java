package ekol.agreement.domain.model.agreement;

import ekol.agreement.domain.dto.agreement.*;
import ekol.agreement.domain.enumaration.AgreementCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Entity
@Table(name = "AgreementLogistic")
@PrimaryKeyJoinColumn(name = "agreement_id")
@Getter
@Setter
@NoArgsConstructor
@Audited
public class AgreementLogistic extends Agreement {


    public AgreementJson toJson() {
        AgreementLogisticJson json = new AgreementLogisticJson();
        json.setId(getId());
        json.setNumber(getNumber());
        json.setName(getName());
        json.setCreatedAt(getCreatedAt());
        json.setCreatedBy(getCreatedBy());
        json.setType(getType());
        json.setStatus(getStatus());
        json.setAccount(getAccount());
        json.setStartDate(getStartDate());
        json.setEndDate(getEndDate());
        json.setServiceAreas(!CollectionUtils.isEmpty(getServiceAreas()) ? new ArrayList<>(getServiceAreas()) : null);
        json.setRenewalLength(getRenewalLength());
        json.setRenewalDateType(getRenewalDateType());
        json.setAutoRenewalLength(getAutoRenewalLength());
        json.setAutoRenewalDateType(getAutoRenewalDateType());
        json.setAutoRenewalDate(getAutoRenewalDate());
        json.setKpiInfos(!CollectionUtils.isEmpty(getKpiInfos()) ? getKpiInfos().stream().map(KpiInfoJson::fromEntity).collect(Collectors.toList()) : null);
        json.setOwnerInfos(!CollectionUtils.isEmpty(getOwnerInfos()) ? getOwnerInfos().stream().map(OwnerInfoJson::fromEntity).collect(Collectors.toList()) : null);
        json.setInsuranceInfos(!CollectionUtils.isEmpty(getInsuranceInfos()) ? getInsuranceInfos().stream().map(InsuranceInfoJson::fromEntity).collect(Collectors.toList()) : null);
        json.setUnitPrices(!CollectionUtils.isEmpty(getUnitPrices()) ? getUnitPrices().stream().map(UnitPriceJson::fromEntity).collect(Collectors.toList()) : null);
        json.setPriceAdaptationModels(!CollectionUtils.isEmpty(getPriceAdaptationModels()) ? getPriceAdaptationModels().stream().map(PriceAdaptationModelJson::fromEntity).collect(Collectors.toList()) : null);
        json.setSignatureInfos(!CollectionUtils.isEmpty(getSignatureInfos()) ? getSignatureInfos().stream().map(SignatureInfoJson::fromEntity).collect(Collectors.toList()) : null);
        json.setFinancialInfo(getFinancialInfo() != null ? FinancialInfoJson.fromEntity(getFinancialInfo()) : null);
        json.setLetterOfGuarentees(!CollectionUtils.isEmpty(getLetterOfGuarentees()) ? getLetterOfGuarentees().stream().map(LetterOfGuarenteeJson::fromEntity).collect(Collectors.toList()) : null);
        json.setLegalInfo(getLegalInfo() != null ? LegalInfoJson.fromEntity(getLegalInfo()) : null);
        json.setNotes(!CollectionUtils.isEmpty(getNotes()) ? getNotes().stream().map(NoteJson::fromEntity).collect(Collectors.toList()) : null);
        json.setDocuments(!CollectionUtils.isEmpty(getDocuments()) ? getDocuments().stream().map(DocumentJson::fromEntity).collect(Collectors.toList()) : null);
        json.setLastUpdatedBy(getLastUpdatedBy());
        json.setDiscriminator(AgreementCategory.LOGISTIC.name());
        return json;
    }
}
