package ekol.crm.account.domain.model.potential;

import ekol.crm.account.domain.dto.CustomsOfficeJson;
import ekol.crm.account.domain.dto.potential.CustomsPotentialJson;
import ekol.crm.account.domain.dto.potential.PotentialJson;
import ekol.crm.account.domain.model.CustomsOffice;
import ekol.crm.account.service.CompanyService;
import ekol.crm.account.util.BeanUtils;
import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue(value = "CCL")
@Getter
@Setter
@NoArgsConstructor
@Audited
public class CustomsPotential extends Potential{


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "potential", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<CustomsOffice> customsOffices = new HashSet<>();

    @Override
    public PotentialJson toJson() {
        CodeNamePair cnp = BeanUtils.getBean(CompanyService.class).findServiceAreaByCode(getServiceArea(),true);
        CustomsPotentialJson json = new CustomsPotentialJson();
        json.setId(getId());
        json.setCompetitor(getCompetitor());
        json.setCustomsType(getCustomsType());
        json.setCustomsOffices(Optional.ofNullable(getCustomsOffices()).orElseGet(Collections::emptySet).stream().map(CustomsOfficeJson::fromEntity).collect(Collectors.toList()));
        json.setFrequencyType(getFrequencyType());
        json.setFrequency(getFrequency());
        json.setValidityStartDate(getValidityStartDate());
        json.setValidityEndDate(getValidityEndDate());
        json.setServiceArea(cnp);
        json.setDiscriminator(cnp.getCode());
        json.setCreatedBy(getCreatedBy());
        json.setCreatedAt(getCreatedAt());
        json.setLastUpdatedBy(getLastUpdatedBy());
        json.setLastUpdated(getLastUpdated());
        return json;
    }
}
