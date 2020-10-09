package ekol.crm.account.domain.dto.potential;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.dto.CountryPointJson;
import ekol.crm.account.domain.enumaration.LoadWeightType;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.crm.account.domain.model.potential.RoadPotential;
import ekol.exceptions.ValidationException;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class RoadPotentialJson extends PotentialJson{

    private LoadWeightType loadWeightType;

    public Potential toEntity(){
        RoadPotential entity = new RoadPotential();
        entity.setId(getId());
        entity.setFromCountry(getFromCountry().toEntity());
        entity.setFromCountryPoint(Optional.ofNullable(getFromPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::toEntity).collect(Collectors.toSet()));
        entity.setToCountry(getToCountry().toEntity());
        entity.setToCountryPoint(Optional.ofNullable(getToPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::toEntity).collect(Collectors.toSet()));
        entity.setShipmentLoadingTypes(!CollectionUtils.isEmpty(getShipmentLoadingTypes()) ? new HashSet<>(getShipmentLoadingTypes()) : null);
        entity.setLoadWeightType(getLoadWeightType());
        entity.setCompetitor(getCompetitor());
        entity.setFrequencyType(getFrequencyType());
        entity.setFrequency(getFrequency());
        entity.setValidityStartDate(getValidityStartDate());
        entity.setValidityEndDate(getValidityEndDate());
        entity.setCreatedAt(getCreatedAt());
        entity.setCreatedBy(getCreatedBy());
        entity.setServiceArea(Optional.of(getServiceArea().getCode()).orElse("ROAD"));
        return entity;
    }

    public void validate(){
        if(getFromPoint() == null){
            throw new ValidationException("From postal should not be empty");
        }
        Optional.ofNullable(getFromPoint()).map(Collection::stream).orElseGet(Stream::empty).forEach(CountryPointJson::validate);
        if(getToPoint() == null){
            throw new ValidationException("To postal should not be empty");
        }
        Optional.ofNullable(getToPoint()).map(Collection::stream).orElseGet(Stream::empty).forEach(CountryPointJson::validate);
        if(CollectionUtils.isEmpty(getShipmentLoadingTypes())){
            throw new ValidationException("Potential should have shipment loading type info");
        }
    }
}
