package ekol.crm.account.domain.model.potential;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.*;

import ekol.crm.account.service.CompanyService;
import ekol.crm.account.util.BeanUtils;
import ekol.model.CodeNamePair;
import org.aspectj.apache.bcel.classfile.Code;
import org.hibernate.envers.Audited;
import org.springframework.util.CollectionUtils;

import ekol.crm.account.domain.dto.CountryJson;
import ekol.crm.account.domain.dto.CountryPointJson;
import ekol.crm.account.domain.dto.potential.PotentialJson;
import ekol.crm.account.domain.dto.potential.RoadPotentialJson;
import ekol.crm.account.domain.enumaration.LoadWeightType;
import ekol.crm.account.domain.model.CountryPoint;
import lombok.*;

@Entity
@DiscriminatorValue(value = "ROAD")
@Getter
@Setter
@NoArgsConstructor
@Audited
public class RoadPotential extends Potential{

    @Enumerated(EnumType.STRING)
    private LoadWeightType loadWeightType;

    @Override
    public PotentialJson toJson() {
        CodeNamePair cnp = BeanUtils.getBean(CompanyService.class).findServiceAreaByCode(getServiceArea(),true);
        RoadPotentialJson json = new RoadPotentialJson();
        json.setId(getId());
        json.setFromCountry(CountryJson.fromEntity(getFromCountry()));
        json.setFromPoint(Optional.ofNullable(getFromCountryPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::fromEntity).collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(CountryPointJson::getName)))));
        json.setToCountry(CountryJson.fromEntity(getToCountry()));
        json.setToPoint(Optional.ofNullable(getToCountryPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::fromEntity).collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(CountryPointJson::getName)))));
        if(!CollectionUtils.isEmpty(getShipmentLoadingTypes())){
            json.setShipmentLoadingTypes(new ArrayList<>(getShipmentLoadingTypes()));
            Collections.sort(json.getShipmentLoadingTypes());
        }
        json.setLoadWeightType(getLoadWeightType());
        json.setCompetitor(getCompetitor());
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
    
    @Override
    public String toString() {
    	return new StringBuilder(getFromCountry().getName())
    		.append(" - ")
    		.append(getFromCountryPoint().isEmpty() ? "ALL" : getFromCountryPoint().stream().map(CountryPoint::getName).collect(Collectors.joining(",")))
    		.append(" => ")
    		.append(getToCountry().getName())
    		.append(" - ")
    		.append(getToCountryPoint().isEmpty() ? "ALL" : getToCountryPoint().stream().map(CountryPoint::getName).collect(Collectors.joining(",")))
    		.toString();
    }
}
