package ekol.crm.account.domain.model.potential;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.*;

import ekol.crm.account.service.CompanyService;
import ekol.crm.account.util.BeanUtils;
import ekol.model.CodeNamePair;
import org.hibernate.envers.Audited;
import org.springframework.util.CollectionUtils;

import ekol.crm.account.domain.dto.CountryJson;
import ekol.crm.account.domain.dto.CountryPointJson;
import ekol.crm.account.domain.dto.potential.AirPotentialJson;
import ekol.crm.account.domain.dto.potential.PotentialJson;
import ekol.crm.account.domain.enumaration.*;
import lombok.*;

@Entity
@DiscriminatorValue(value = "AIR")
@Getter
@Setter
@NoArgsConstructor
@Audited
public class AirPotential extends Potential{

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "Crm_Pt_Charge_Weight", joinColumns = @JoinColumn(name = "potential_id"))
    @Column(name = "chargeableWeight")
    private Set<ChargeableWeight> chargeableWeights = new HashSet<>();

    @Column
    private String incoterm;

    @Enumerated(EnumType.STRING)
    private IncotermExplanation incotermExplanation;

    @Override
    public PotentialJson toJson() {
        CodeNamePair cnp = BeanUtils.getBean(CompanyService.class).findServiceAreaByCode(getServiceArea(),true);
        AirPotentialJson json = new AirPotentialJson();
        json.setId(getId());
        json.setFromCountry(CountryJson.fromEntity(getFromCountry()));
        json.setFromPoint(Optional.ofNullable(getFromCountryPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::fromEntity).collect(Collectors.toSet()));
        json.setToCountry(CountryJson.fromEntity(getToCountry()));
        json.setToPoint(Optional.ofNullable(getToCountryPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::fromEntity).collect(Collectors.toSet()));
        json.setFrequencyType(getFrequencyType());
        json.setFrequency(getFrequency());
        if(!CollectionUtils.isEmpty(getChargeableWeights())){
            json.setChargeableWeights(new ArrayList<>(getChargeableWeights()));
            Collections.sort(json.getChargeableWeights());
        }
        json.setIncoterm(getIncoterm());
        json.setIncotermExplanation(getIncotermExplanation());
        json.setCompetitor(getCompetitor());
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
