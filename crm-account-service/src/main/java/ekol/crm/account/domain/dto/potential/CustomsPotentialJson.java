package ekol.crm.account.domain.dto.potential;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.dto.CustomsOfficeJson;
import ekol.crm.account.domain.enumaration.CustomsType;
import ekol.crm.account.domain.model.CustomsOffice;
import ekol.crm.account.domain.model.potential.CustomsPotential;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.exceptions.ValidationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class CustomsPotentialJson extends PotentialJson{

    private CustomsType customsType;
    private List<CustomsOfficeJson> customsOffices;
    private String discriminator;

    public Potential toEntity(){
        CustomsPotential entity = new CustomsPotential();
        entity.setId(getId());
        entity.setFrequencyType(getFrequencyType());
        entity.setFrequency(getFrequency());
        entity.setCompetitor(getCompetitor());
        entity.setCustomsType(getCustomsType());
        Optional.ofNullable(getCustomsOffices()).orElseGet(Collections::emptyList).stream().forEach(customsOfficeJson -> {
            CustomsOffice customsOffice = new CustomsOffice();
            customsOffice.setId(customsOfficeJson.getId());
            customsOffice.setOffice(customsOfficeJson.getOffice());
            customsOffice.setPotential(entity);
            entity.getCustomsOffices().add(customsOffice);
        });
        entity.setValidityStartDate(getValidityStartDate());
        entity.setValidityEndDate(getValidityEndDate());
        entity.setCreatedAt(getCreatedAt());
        entity.setCreatedBy(getCreatedBy());
        entity.setServiceArea(Optional.of(getServiceArea().getCode()).orElse("CCL"));
        return entity;
    }

    public void validate(){
        if(getCustomsType() == null){
            throw new ValidationException("Import/Export should not be empty");
        }
        if(CollectionUtils.isEmpty(getCustomsOffices())){
            throw new ValidationException("Customs offices should not be empty");
        }
    }
}
