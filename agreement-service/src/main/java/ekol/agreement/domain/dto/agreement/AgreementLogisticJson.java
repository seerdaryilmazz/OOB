package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.domain.model.agreement.AgreementLogistic;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class AgreementLogisticJson extends AgreementJson {

    @Override
    public Agreement toEntity() {
        AgreementLogistic entity = new AgreementLogistic();
        entity.setId(getId());
        entity.setNumber(getNumber());
        entity.setName(getName());
        entity.setCreatedAt(getCreatedAt());
        entity.setCreatedBy(getCreatedBy());
        entity.setType(getType());
        entity.setStatus(getStatus());
        entity.setAccount(getAccount());
        entity.setStartDate(getStartDate());
        entity.setEndDate(getEndDate());
        entity.setServiceAreas(!CollectionUtils.isEmpty(getServiceAreas()) ? new HashSet<>(getServiceAreas()) : null);
        entity.setRenewalLength(getRenewalLength());
        entity.setRenewalDateType(getRenewalDateType());
        entity.setAutoRenewalLength(getAutoRenewalLength());
        entity.setAutoRenewalDateType(getAutoRenewalDateType());
        entity.setAutoRenewalDate(getAutoRenewalDate());
        entity.setKpiInfos(!CollectionUtils.isEmpty(getKpiInfos()) ? getKpiInfos().stream().map(KpiInfoJson::toEntity).collect(Collectors.toSet()) : null);
        entity.setOwnerInfos(!CollectionUtils.isEmpty(getOwnerInfos()) ? getOwnerInfos().stream().map(OwnerInfoJson::toEntity).collect(Collectors.toSet()) : null);
        entity.setInsuranceInfos(!CollectionUtils.isEmpty(getInsuranceInfos()) ? getInsuranceInfos().stream().map(InsuranceInfoJson::toEntity).collect(Collectors.toSet()) : null);
        entity.setUnitPrices(!CollectionUtils.isEmpty(getUnitPrices()) ? getUnitPrices().stream().map(UnitPriceJson::toEntity).collect(Collectors.toSet()) : null);
        entity.setPriceAdaptationModels(!CollectionUtils.isEmpty(getPriceAdaptationModels()) ? getPriceAdaptationModels().stream().map(PriceAdaptationModelJson::toEntity).collect(Collectors.toSet()) : null);
        entity.setSignatureInfos(!CollectionUtils.isEmpty(getSignatureInfos()) ? getSignatureInfos().stream().map(SignatureInfoJson::toEntity).collect(Collectors.toSet()) : null);
        entity.setFinancialInfo(getFinancialInfo() != null ? getFinancialInfo().toEntity() : null);
        entity.setLetterOfGuarentees(!CollectionUtils.isEmpty(getLetterOfGuarentees()) ? getLetterOfGuarentees().stream().map(LetterOfGuarenteeJson::toEntity).collect(Collectors.toSet()) : null);
        entity.setLegalInfo(getLegalInfo() != null ? getLegalInfo().toEntity() : null);
        entity.setNotes(!CollectionUtils.isEmpty(getNotes()) ? getNotes().stream().map(NoteJson::toEntity).collect(Collectors.toList()) : null);
        entity.setDocuments(!CollectionUtils.isEmpty(getDocuments()) ? getDocuments().stream().map(DocumentJson::toEntity).collect(Collectors.toList()) : null);
        return entity;
    }
}
